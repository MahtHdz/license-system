#include "../main.h"

/* Define the namespace */
using namespace std;

/*****************************************************
   Returns the product ID of the first physical drive.
*****************************************************/
string getHDD_ID(void){
    Operations SysOps;
    SysOps.errorMsg = "error: getHDD_ID function, ";

    string empty = {};
    /* Get a handler for the 1st physical drive */
    HANDLE handler = CreateFileW(L"\\\\.\\PhysicalDrive0", 0, FILE_SHARE_READ, NULL, OPEN_EXISTING, 0, NULL);
    if (handler == INVALID_HANDLE_VALUE){
        SysOps.errorMsg += "CreateFileW() => INVALID_HANDLE_VALUE";
        SysOps.throwError();
    }

    /*
        An unique_ptr is used to perform cleanup automatically
        when returning (i.e. to avoid code duplication).
    */
    unique_ptr<remove_pointer<HANDLE>::type, void (*)(HANDLE)> hDevice{handler, [](HANDLE handle) { CloseHandle(handle); }};

    /*
        Initialize a STORAGE_PROPERTY_QUERY data
        structure (to be used as input to DeviceIoControl).
    */
    STORAGE_PROPERTY_QUERY storagePropertyQuery{};
    storagePropertyQuery.PropertyId = StorageDeviceProperty;
    storagePropertyQuery.QueryType = PropertyStandardQuery;

    /*
        Initialize a STORAGE_DESCRIPTOR_HEADER data structure
        (to be used as output from DeviceIoControl).
    */
    STORAGE_DESCRIPTOR_HEADER storageDescriptorHeader{};

    /*
        The next call to DeviceIoControl retrieves necessary
        size (in order to allocate a suitable buffer) call
        DeviceIoControl and return an empty string on failure.
    */
    DWORD dwBytesReturned = 0;
    if (!DeviceIoControl(hDevice.get(), IOCTL_STORAGE_QUERY_PROPERTY, &storagePropertyQuery, sizeof(STORAGE_PROPERTY_QUERY),
                         &storageDescriptorHeader, sizeof(STORAGE_DESCRIPTOR_HEADER), &dwBytesReturned, NULL)){
        SysOps.errorMsg += "first DeviceIoControl() => failed to send the control code to a specified device driver.";
        SysOps.throwError();
        //throw "failed to send the control code to a specified device driver.";
    }
    /* Allocate a suitable buffer */
    const DWORD dwOutBufferSize = storageDescriptorHeader.Size;
    unique_ptr<BYTE[]> pOutBuffer{new BYTE[dwOutBufferSize]{}};
    /* Call DeviceIoControl with the allocated buffer */
    if (!DeviceIoControl(hDevice.get(), IOCTL_STORAGE_QUERY_PROPERTY, &storagePropertyQuery, sizeof(STORAGE_PROPERTY_QUERY),
                         pOutBuffer.get(), dwOutBufferSize, &dwBytesReturned, NULL)){

        SysOps.errorMsg += "second DeviceIoControl() => failed to send the control code to a specified device driver.";
        SysOps.throwError();
        //throw "failed to send the control code to a specified device driver.";
    }
    /* Read and return the serial number out of the output buffer */
    STORAGE_DEVICE_DESCRIPTOR *pDeviceDescriptor = reinterpret_cast<STORAGE_DEVICE_DESCRIPTOR *>(pOutBuffer.get());
    const DWORD dwSerialNumberOffset = pDeviceDescriptor -> SerialNumberOffset;
    const char *serialNo = reinterpret_cast<const char *>(pOutBuffer.get() + dwSerialNumberOffset);
    SysOps.successMsg = "HDD ID succefully obtained.";
    SysOps.opSuccess();
    return serialNo;
}
