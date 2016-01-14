package ru.jtconsulting.voicerecognition2;

/**
 * Created by www on 14.01.2016.
 */
public class Constants {
    /**
     *  return initService()
     */
    public static final int INITSERVICE_SDK_NOT_INITED=0;
    public static final int INITSERVICE_INITIALIZATION_PROCESSING=1;
    public static final int INITSERVICE_INITIALIZATION_COMPLETED=2;
    public static final int INITSERVICE_ERROR_INITIALIZATION=3;

    /**
     *  return getServiceState
     */
    public final static  int GETSERVICESTATE_SERVICE_NOT_INITED = 0;
    public final static  int GETSERVICESTATE_SERVICE_PROCESSING_INITIALIZATION = 1;
    public final static  int GETSERVICESTATE_SERVICE_READY_TO_WORK = 2;
    public final static  int GETSERVICESTATE_SERVICE_ERROR_INIT = 3;
    public final static  int GETSERVICESTATE_SERVICE_BUSY = 4;


    /**
     *  return  getServiceAvailability()
     */
    public final static  int GETSERVICEAVAILABILITY_SERVICES_ARE_NOT_AVAILABLE = 0;
    public final static  int GETSERVICEAVAILABILITY_AVAILABLE_LVSCR_SERVICE = 1;
    public final static  int GETSERVICEAVAILABILITY_AVAILABLE_VERIFICATION_SERVICE = 1;
    public final static  int GETSERVICEAVAILABILITY_AVAILABLE_BOTH_SERVICES = 12;

    /**
     *  return  startRecognition() or stopRecognition()
     */

    public final static  int RECOGNITION_WAS_STOPPED = 10;

    /**
     *  return    getLastErrorCode()
     */
    public final static  int GETLASTERRORCODE_NO_ERROR = 0;
    public final static  int GETLASTERRORCODE_YES_ERROR = 1;
}
