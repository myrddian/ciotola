/*
 * Copyright (c) 2021.  Enzo Reyes Licensed under the Apache License, Version 2.0 (the "License");   you may
 * not use this file except in compliance with the License.   You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package ciotola;

import ciotola.actor.CiotolaDirector;
import ciotola.core.ContainerCtx;

import ciotola.core.DefaultContainerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ciotola {

  // Program Strings
  public static final String GENERIC_SERVICE_START_ERROR = "Unable to start service";
  public static final String SERVICE_METHOD_ACCESS_ERROR = "Service Name Method not Accessible";
  public static final String SERVICE_INVOCATION_ERROR = "Service Name invocation error";
  public static final String SERVICE_INIT_METHOD_ERROR =
      "Service specification is invalid, missing method decorators";
  public static final String CLASS_LOADING_ERROR = "Unable to load class";
  public static final String GENERAL_IO_ERROR = "An exception has occurred, IO Exception";
  public static final String CONNECTION_TIMEOUT = "The connection has timed out";
  public static final String MISSING_CONFIG_FILE_ERROR =
      "Configuration file is missing or not specified";
  public static final String UKNONWN_PARAMETERS = "Unable to parse the parameters specified";
  public static final String URL_CLASS_LOADER = "The URL is malformed or invalid while scanning";
  public static final String CLASS_LOADER_ERROR = "An error has been raised while loading classes";
  public static final String UNKNOWN_COMPONENT_SPECIFIED =
      "While injecting, a component was specified that cannot be found";
  public static final String FIELD_INJECTION_ERROR = "Unable to inject into the specified field";
  public static final String LOGGER_FIELD_INJECTION_ERROR =
      "While injecting the logger there has been an error";
  public static final String INVALID_CLASS_LOADER =
      "No class loader specified while starting container";
  public static final String CLASS_SCANNER_MISSING = "No JavaClassScanner specified - aborting";
  public static final int DEFAULT_INT_SIZE = 4;
  public static final int VARIABLE_LENGTH = 0;
  public static final int SHORT_LENGTH = 2;
  public static final int LONG_LENGTH = 8;
  public static final int INITIAL_POSITION = 0;
  private static Ciotola SINGLE_INSTANCE = null;
  private final Logger logger = LoggerFactory.getLogger(Ciotola.class);
  private final DefaultContainerContext containerContext = new DefaultContainerContext();

  private Ciotola() {
  }

  public static Ciotola getInstance() {
    if (SINGLE_INSTANCE == null) {
      synchronized (Ciotola.class) {
        if (SINGLE_INSTANCE == null) {
          SINGLE_INSTANCE = new Ciotola();
        }
      }
    }
    return SINGLE_INSTANCE;
  }

  public ContainerCtx getDefaultContainerContext() {
    return this.containerContext;
  }

  public CiotolaDirector getDirector() {
    return this.containerContext.getDirector();
  }

  public static ContainerCtx getDefault() {
    return getInstance().getDefaultContainerContext();
  }

  public static CiotolaDirector defaultDirector() {
    return getDefault().getDirector();
  }

  public enum ParseType {
    UNSIGNED_SHORT_TO_INT,
    UNSIGNED_INT_TO_LONG,
    SIGNED_SHORT,
    SIGNED_INT,
    SIGNED_LONG
  }

  public enum ByteOrder {
    LITTLE_ENDIAN,
    BIG_ENDIAN
  }

  public enum ServiceStatus {
    SERVICE_START,
    SERVICE_STOP,
    SERVICE_RESTART,
    SERVICE_RUNNING,
    SERVICE_STATUS
  }

}
