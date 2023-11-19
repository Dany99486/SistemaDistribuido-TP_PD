javac -d out\production\serverBackup src\pt\isec\PD\Model\*.java src\pt\isec\PD\RMI\*.java
java -cp ".;out\production\serverBackup;.m2\repository\org\xerial\sqlite-jdbc\3.42.0.0\sqlite-jdbc-3.42.0.0.jar" pt.isec.PD.Model.MainServerBackup resources
