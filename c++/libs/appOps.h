#ifndef _APPOPS
#define _APPOPS
class Operations{
    public:
        LogFile log;
        std::string successMsg;
        std::string errorMsg;
        void throwError(void);
        void opSuccess(void);
        char *byteToHex(unsigned char b, char *ptr);

    private:
};
#endif