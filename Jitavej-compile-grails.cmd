@java -Xms256m -Xmx256M -cp "%~dp0\src;%~dp0\bin;C:/Users/Tee/Desktop/Dev/gwt-1.5.2/gwt-user.jar;C:/Users/Tee/Desktop/Dev/gwt-1.5.2/gwt-dev-windows.jar;C:/Users/Tee/Desktop/apps/jitavej-client/lib/gwt-incubator.jar;C:/Users/Tee/Desktop/apps/jitavej-client/lib/gwtext.jar" com.google.gwt.dev.GWTCompiler -out "C:/Users/Tee/Desktop/apps/jitavej/web-app/gwt" %* com.neural.jitavej.Jitavej

cd "C:/Users/Tee/Desktop/apps/jitavej"

grails war

rm -rf "C:/Users/Tee/Desktop/apps/jitavej/jitavej.war"

mv "C:/Users/Tee/Desktop/apps/jitavejjitavej-0.1.war" "C:/Users/Tee/Desktop/apps/jitavej/jitavej.war"

cd "C:/Users/Tee/Desktop/apps/jitavej-client"