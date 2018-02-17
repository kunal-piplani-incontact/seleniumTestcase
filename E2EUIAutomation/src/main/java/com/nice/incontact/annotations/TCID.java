package com.nice.incontact.annotations;

import java.lang.annotation.*;
/**
 * This annotation is for keeping a track of test cases with their IDs.
 *
 */
@Retention(RetentionPolicy.RUNTIME)

public @interface TCID {
	String value();
}
