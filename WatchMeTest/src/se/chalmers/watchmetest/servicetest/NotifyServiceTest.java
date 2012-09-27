package se.chalmers.watchmetest.activitytest;

import android.test.ServiceTestCase;

/**
 *	NotifyServiceTest.java
 *
 *	@author Johan
 */

public class NotifyServiceTest extends ServiceTestCase<T> {
	
	public NotifyServiceTest() {
		super(NotifyService.class);
	}
	
	public NotifyServiceTest(Class<NotifyService> serviceClass) {
		super(serviceClass);
	}
	
	@Override
	public void setUp() {
		super.setUp();
	}
	
}
