#include "main.h"

extern "C" DLL_EXPORT BOOL APIENTRY DllMain(HINSTANCE hinstDLL, DWORD fdwReason, LPVOID lpvReserved){
    switch (fdwReason){
        case DLL_PROCESS_ATTACH:
            // attach to process
            // return FALSE to fail DLL load
            break;

        case DLL_PROCESS_DETACH:
            // detach from process
            break;

        case DLL_THREAD_ATTACH:
            // attach to thread
            break;

        case DLL_THREAD_DETACH:
            // detach from thread
            break;
    }
    return TRUE; // succesful
}

JNIEXPORT jstring JNICALL
Java_ids_IDsCollector_getHDD_1ID(JNIEnv *env, jobject obj){
    std::string ID;
    try{
        ID = getHDD_ID();
    }catch(std::exception& e){
        jclass exception = env->FindClass(JAVA_EXCEPTION_PATH);
        env->ThrowNew(exception, e.what());
        return NULL;
    }
    return env->NewStringUTF(ID.c_str());
}

JNIEXPORT jstring JNICALL
Java_ids_IDsCollector_getBIOS_1ID(JNIEnv *env, jobject obj){
    std::string ID;
    try{
        ID = getSMBIOS_UUID();
    }catch(std::exception& e){
        jclass exception = env->FindClass(JAVA_EXCEPTION_PATH);
        env->ThrowNew(exception, e.what());
        return NULL;
    }
    return env->NewStringUTF(ID.c_str());
}
