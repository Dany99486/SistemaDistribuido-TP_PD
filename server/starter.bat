javac -d out\production\server src\pt\isec\PD\Model\*.java src\pt\isec\PD\RMI\*.java src\pt\isec\PD\UI\*.java src\pt\isec\PD\Main.java
java -cp ".;out\production\server;.m2\repository\org\xerial\sqlite-jdbc\3.42.0.0\sqlite-jdbc-3.42.0.0.jar" pt.isec.PD.Main 5100 resources rmiService 5200
