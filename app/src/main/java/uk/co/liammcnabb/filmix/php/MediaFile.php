<?php

class MediaFile {

        function validateFile() {
            if(!file_exists($_SESSION['media'])) {
                header("location: media.php");
            }
        }

}

?>
