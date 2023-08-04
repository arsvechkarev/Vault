package com.arsvechkarev.vaultdesktop;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.WString;
import javafx.application.Application;

public class VaultLauncher {

  static {
    Native.register("shell32");
  }

  public static void main(String[] args) {
    setCurrentProcessExplicitAppUserModelID(VaultDesktopApplication.class.getName());
    Application.launch(VaultDesktopApplication.class);
  }

  // Trying to set application id to make Windows recognize .exe file as a
  // separate program, but now does not work
  // https://stackoverflow.com/questions/1907735/using-jna-to-get-set-application-identifier
  public static void setCurrentProcessExplicitAppUserModelID(final String appID) {
    if (SetCurrentProcessExplicitAppUserModelID(new WString(appID)).longValue() != 0) {
      throw new RuntimeException(
          "unable to set current process explicit AppUserModelID to: " + appID);
    }
  }

  private static native NativeLong SetCurrentProcessExplicitAppUserModelID(WString appID);
}
