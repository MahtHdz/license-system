#include "../main.h"

using namespace std;

#define PATH_MAX 260
#define FILENAME "log\\SysIDs.log"

string LogFile::eventLog(string message){
    string date, dateTime, path, finalMessage;

    // current date/time based on current system
    time_t now = time(0);

    // Get the individual components
    tm *ltm = localtime(&now);
    date.append(to_string(ltm->tm_mday));
    date.append("/");
    date.append(to_string(ltm->tm_mon + 1));
    date.append("/");
    date.append(to_string(ltm->tm_year + 1900));

    dateTime.append(to_string(ltm->tm_hour));
    dateTime.append(":");
    dateTime.append(to_string(ltm->tm_min));
    dateTime.append(":");
    dateTime.append(to_string(ltm->tm_sec));

    char buff[PATH_MAX];
    _getcwd(buff, PATH_MAX);
    string current_working_dir(buff);

    finalMessage = "[" + dateTime + "] " + date + "\t" +
                   "location: " + current_working_dir +
                   "\t" + "event > " + message;

    return finalMessage;
}

void LogFile::writeToLog(){
    ofstream log;
    log.open(FILENAME, ios::app);
    string fMessage = eventLog(message);
    log << fMessage << endl;
    log.close();
}

void LogFile::execLogProcess(){
    InitializeCriticalSection(&m_cs);
    EnterCriticalSection(&m_cs);
    std::thread t([this] {this -> writeToLog();});
    t.join();
    LeaveCriticalSection(&m_cs);
    DeleteCriticalSection(&m_cs);
}
