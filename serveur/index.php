<?php
    require_once("config.php");
    require_once("database.php");
    
    if(isset($_POST['txtNotification']) && !empty($_POST['txtNotification']))
    {
        $data = array('notification' => $_POST['txtNotification']);
        
        sql_open();
        
        $result = sql_query("
                            SELECT F_REGISTRATION_ID
                            FROM T_APPLICATION_CLIENT
                            ");
                            
        while ($row = sql_fetch_array($result))
        {
            $ids[] = $row[0];
        }
                            
        sql_close();

        sendGoogleCloudMessage($data, $ids);
    }
    
    function sendGoogleCloudMessage($data, $ids)
    {
        $apiKey = 'AIzaSyA0bNx7ujc0Lzzsrjws5F3XdZ80eY5VFBo';
        $url = 'https://android.googleapis.com/gcm/send';
        $post = array('registration_ids'  => $ids,
                      'data'              => $data,
                     );

        $headers = array('Authorization: key=' . $apiKey,
                         'Content-Type: application/json'
                        );

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($post));

        $result = curl_exec($ch);

        if(curl_errno($ch))
            {echo 'GCM error: '.curl_error($ch);}
        
        curl_close($ch);
        echo $result;
    }
?>

<html>
    <head>
        <title>My notification server</title>
    </head>
    <body>
        <form method="POST" action="#">
            <input type="text" name="txtNotification"/>
            <input type="submit" value="notify" />
        </form>
    </body>
</html>