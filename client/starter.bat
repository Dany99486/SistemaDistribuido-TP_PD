

javac -d out\production\client src\pt\isec\PD\ClientUI\Controllers\*.java src\pt\isec\PD\ClientUI\Model\*.java src\pt\isec\PD\ClientUI\MainJFX.java src\pt\isec\PD\*.java src\pt\isec\PD\ClientUI\Views\*.fxml
echo compilado
java -cp ".;out\production\client;.m2\repository\org\lib\*.jar" pt.isec.PD.Main localhost 5100
