/*******************************
  Module to get the smBIOS UUID
 *******************************/

// Load the main library with all the external resources.
#include "../main.h"

// Define the namespace.
using namespace std;

Operations SysOps;

/*
  Get smBIOS UUID in a 16 byte array.
*/
void byteUUID(unsigned char *uuid){
  SysOps.errorMsg = "error: ByteUUID function, ";

  // Firmware Table Provider Signature. (RSMB)
  DWORD FTPS = 1381190978;
  /*
    Pointer to a buffer that receives
    the requested firmware table.
  */
  RawSMBIOSData *smBiosData=nullptr;
  BYTE *data;
  // Size of system fimware table structure.
  DWORD smBiosDataSize = 0;
  /*
    Flag to know if the function GetSystemFirmwareTable()
    return the table structure or return an error.
  */
  DWORD smBiosFlag = 0;

  // Get smBiosDataSize of smBIOS table
  smBiosDataSize = GetSystemFirmwareTable (FTPS, 0, NULL, 0);
  /*
    Throw exeception if the program cannot get
    the size of the system firmware table.
  */
  if(smBiosDataSize == 0){
    SysOps.errorMsg += "first GetSystemFirmwareTable() => cannot access to system firmware table.";
    SysOps.throwError();
  }

  // Assign memory dynamically to get the system firmware structure.
  smBiosData = (RawSMBIOSData *)malloc(smBiosDataSize);
  /*
    Throw exeception if the there is
    insufficient memory avalible in
    the system.
  */
  if(smBiosData == NULL){
    SysOps.errorMsg += "malloc() => insufficient memory avalible.";
    SysOps.throwError();
  }

  // Get SMBIOS table
  smBiosFlag = GetSystemFirmwareTable (FTPS, 0, smBiosData, smBiosDataSize);
  /*
    Throw exeception if the program cannot
    access to the system firmware table.
  */
  if (smBiosFlag == 0){
    SysOps.errorMsg += "second GetSystemFirmwareTable() => cannot access to system firmware table.";
    SysOps.throwError();
  }
  //Go through SMBIOS structures
  data = smBiosData->SMBIOSTableData;
  while (data < smBiosData->SMBIOSTableData + smBiosData->Length){
    BYTE *next;
    dmi_header *h = (dmi_header*)data;

    if (h->length < 4)
      break;

    //Search for System Information structure with type 0x01 (see para 7.2)
    if (h->type == 0x01 && h->length >= 0x19){
      data += 0x08; //UUID is at offset 0x08

      // check if there is a valid UUID (not all 0x00 or all 0xff)
      bool all_zero = true, all_one = true;
      for (int i = 0; i < 16 && (all_zero || all_one); i++){
        if (data[i] != 0x00) all_zero = false;
        if (data[i] != 0xFF) all_one = false;
      }
      if (!all_zero && !all_one){
        /* As off version 2.6 of the SMBIOS specification, the first 3 fields
        of the UUID are supposed to be encoded on little-endian. (para 7.2.1) */
        *uuid++ = data[3];
        *uuid++ = data[2];
        *uuid++ = data[1];
        *uuid++ = data[0];
        *uuid++ = data[5];
        *uuid++ = data[4];
        *uuid++ = data[7];
        *uuid++ = data[6];
        for (int i = 8; i < 16; i++)
          *uuid++ = data[i];
      }
      break;
    }

    //skip over formatted area
    next = data + h->length;

    //skip over unformatted area of the structure (marker is 0000h)
    while (next < smBiosData->SMBIOSTableData + smBiosData->Length && (next[0] != 0 || next[1] != 0))
      next++;
    next += 2;

    data = next;
  }
  free (smBiosData);
}

/*  */
string getSMBIOS_UUID(){
  BYTE uuid[16];
  string BIOS_ID;
  byteUUID(uuid);
  char uuid_str[40];
  char *ptr = uuid_str;

  for (size_t i = 0; i < 16; i++){
    ptr = SysOps.byteToHex(uuid[i], ptr);
    if (i == 3 || i == 5 || i == 7 || i == 9)
      *ptr++ = '-';
  }
  *ptr++ = 0;

  BIOS_ID = uuid_str;
  SysOps.successMsg = "BIOS ID succefully obtained.";
  SysOps.opSuccess();
  return BIOS_ID;
}
