<?php
require_once("config.php");
require_once("database.php");

$data = json_decode(utf8_encode(file_get_contents('php://input')));

if(isset($data->id) && !empty($data->id))
{
    $id = sql_safe($data->id);
    
    sql_open();
    
    $result = sql_query("
                        INSERT INTO T_APPLICATION_CLIENT(F_REGISTRATION_ID)
                        VALUES('" . $id . "')
                        ");
    
    sql_close();
                        
    if($result)
        $response["success"] = 1;
    else
        $response["success"] = 0;
                        
    echo json_encode($response);
    die();
}
else
{
    $response["success"] = 0;

    echo json_encode($response);
    die();
}