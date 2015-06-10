<?php

require 'Mysql.php';

class Membership {

	function validate_user($un, $pwd) {
		$mysql = new Mysql();
		$ensure_credentials = $mysql->verify_Username_and_Pass($un, md5($pwd));
		
		if($ensure_credentials) {
            setcookie("authorized", "true", time()+64800);
                header("location: media.php");
		} else return "Please enter a correct username and password";
	}

	function log_User_Out() {
        
            if(isset($_COOKIE["authorized"])) {
                setcookie("authorized", '', time() - 100000);
            }
	}

	function confirm_Member() {
        if($_COOKIE["authorized"] != "true") header("location: login.php");
	}

    function signup_user($un, $pwd) {
		$mysql = new Mysql();
		$signup = $mysql->add_User($un, md5($pwd));
        
		if($signup) {
			header("location: login.php");
		} else return "Unable to sign up";
	}
    
    function changePassword($oldPass, $newPass, $newPass2) {
        if($newPass != $newPass2) {
            return "Passwords do not match";
        }
        
        $mysql = new Mysql();
        $un = $_COOKIE["userid"];
        $ensure_credentials = $mysql->verify_Username_and_Pass($un, md5($oldPass));
        
        if(!$ensure_credentials) {
            return "Please enter your current password correctly";
        }
        
        $changePass = $mysql->updatePassword($un, md5($newPass));
        
        if($changePass) {
            return "Success Password Changed";
        } else {
            return "Something Went wrong, Password not changed";
        }
        
    }
    
    function addActivity($path){
        $mysql = new Mysql();
        $uid = $_COOKIE["userid"];
        $mysql->insertActivity($uid, $path);
        return true;
    }
    
    function hasWatched($path){
        $mysql = new Mysql();
        $uid = $_COOKIE["userid"];
        $watched = $mysql->checkWatched($uid, $path);
        if($watched){
            return true;
        } else {
            return false;
        }
    }
    
    function getMoviesByName()
    {
        $mysql = new Mysql();
        return $mysql->getMoviesByName();
    }
    
    function getMoviesByYear()
    {
        $mysql = new Mysql();
        return $mysql->getMoviesByYear();
    }
    
    function getMoviesByRating()
    {
        $mysql = new Mysql();
        return $mysql->getMoviesByRating();
    }
}