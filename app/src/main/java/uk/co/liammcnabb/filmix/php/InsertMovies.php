<?php
    require_once 'UpdateDatabase.php';
    
    $update = new UpdateDatabase();
    $update->insertMovies("/var/www/files/media/movies");