#include "../main.h"

/* Define the namespace */
using namespace std;

//
void Operations::throwError(void){
    log.message = errorMsg;
    log.execLogProcess();
    throw runtime_error(errorMsg);
}

//
void Operations::opSuccess(void){
    log.message = successMsg;
    log.execLogProcess();
}

//
char *Operations::byteToHex(BYTE b, char *ptr){
    static const char* digits{ "0123456789ABCDEF" };
    *ptr++ = digits[b >> 4];
    *ptr++ = digits[b & 0x0f];
    return ptr;
}
