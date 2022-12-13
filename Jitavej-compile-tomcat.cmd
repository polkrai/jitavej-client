rm -rf "C:/Tomcat 7.0/webapps/jitavej/gwt/.gwt-tmp"

rm -rf "C:/Tomcat 7.0/webapps/jitavej/gwt/com.neural.jitavej.Jitavej"

@java  -Xms512m -Xmx1024M -cp "%~dp0\src;%~dp0\bin;C:/Users/Tee/Desktop/Dev/gwt-1.5.2/gwt-user.jar;C:/Users/Tee/Desktop/Dev/gwt-1.5.2/gwt-dev-windows.jar;C:/Users/Tee/Desktop/apps/jitavej-client/lib/gwt-incubator.jar;C:/Users/Tee/Desktop/apps/jitavej-client/lib/gwtext.jar" com.google.gwt.dev.GWTCompiler -out "C:/Tomcat 7.0/webapps/jitavej/gwt" %* com.neural.jitavej.Jitavej