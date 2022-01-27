package woyou.aidlservice.jiuiv5;

/**
  * Callback of print service execution results
  */
interface ICallback {

/**
* Returns the result of the interface execution
* Note: This callback only indicates whether the interface execution is successful but does not indicate the working result of the printer. If you need to obtain the printer result, please use the transaction mode.
* @param isSuccess: true execution succeeded, false execution failed
*/
oneway void onRunResult(boolean isSuccess);

/**
* Returns the result of the interface execution (string data)
* @param result: Result, print length, etc. since the printer was powered on (in mm)
*/
oneway void onReturnString(String result);

/**
* Returns the specific reason for an exception when the interface fails to execute.
* code: exception code
* msg: exception description
*/
oneway void onRaiseException(int code, String msg);

/**
* Return to printer results
* code: exception code 0 success 1 failure
* msg: exception description
*/
oneway void onPrintResult(int code, String msg);

}