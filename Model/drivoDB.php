<?php
abstract class DrivoDB {
    private static $server   = 'mysql-38e076b7-mi-proyecto-spring.f.aivencloud.com'; 
    private static $port     = '13308';             
    private static $db       = 'drivo';         
    private static $user     = 'avnadmin';          
    private static $password = 'AVNS_14wk8PPGeoqwICxVLZY'; 

    public static function connectDB() {
        try {
            $dsn = "mysql:host=" . self::$server . ";port=" . self::$port . ";dbname=" . self::$db . ";charset=utf8";
            
            $connection = new PDO($dsn, self::$user, self::$password);
            $connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        } catch (PDOException $e) {
            echo "No se ha podido establecer conexión con el servidor de bases de datos de Aiven.<br>";
            die ("Error: " . $e->getMessage());
        }
        return $connection;
    }
}