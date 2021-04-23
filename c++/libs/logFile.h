#ifndef _LOGFILE
#define _LOGFILE

class LogFile{

public:
	void execLogProcess();
	std::string message;
private:
	RTL_CRITICAL_SECTION m_cs;
	void writeToLog();
	std::string eventLog(std::string message);
};
#endif
