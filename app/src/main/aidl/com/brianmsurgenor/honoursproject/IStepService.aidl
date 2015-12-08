// IStepService.aidl
package com.brianmsurgenor.honoursproject;

// Declare any non-default types here with import statements

import com.brianmsurgenor.honoursproject.IStepServiceCallback;

interface IStepService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */


	boolean isRunning();
	void setSensitivity(int sens);
	void registerCallback(IStepServiceCallback cb);
	void unregisterCallback(IStepServiceCallback cb);
}
