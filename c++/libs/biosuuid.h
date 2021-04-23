#ifndef _SMBIOS_ID
#define _SMBIOS_ID

// Retrieve smBIOS UUUID. Return true if successful
std::string getSMBIOS_UUID(void);

/*
  SMBIOS Structure header as described at
  https://www.dmtf.org/sites/default/files/standards/documents/DSP0134_3.3.0.pdf
  (para 6.1.2)
*/
struct dmi_header
{
    unsigned char type;
    unsigned char length;
    unsigned short handle;
    unsigned char data[1];
};

/*
  Structure needed to get the SMBIOS table using GetSystemFirmwareTable API.
  see https://docs.microsoft.com/en-us/windows/win32/api/sysinfoapi/nf-sysinfoapi-getsystemfirmwaretable
*/
struct RawSMBIOSData
{
    unsigned char Used20CallingMethod;
    unsigned char SMBIOSMajorVersion;
    unsigned char SMBIOSMinorVersion;
    unsigned char DmiRevision;
    unsigned long Length;
    unsigned char SMBIOSTableData[1];
};

#endif
