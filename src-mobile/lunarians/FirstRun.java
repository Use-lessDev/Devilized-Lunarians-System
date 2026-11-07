package lunarians;

import arc.Core;
import arc.Settings;
import arc.util.Log;

public class FirstRun {
  private static final String DONE = "lunarians-first-run-done";
  
  public static void run() {
    String str = "lunarians-first-run-done";
    try {
      Settings settings = Core.settings;
      Integer integer = null;
      return;
    } finally {
      str = null;
      Log.err("[Lunarians] FirstRun", (Throwable)str);
    } 
  }
}
