<?php
    require_once 'UpdateDatabase.php';
    
    $update = new UpdateDatabase();
    $update->updateDirectory("/var/www/files/media/movies");