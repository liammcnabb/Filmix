<?php

require_once 'constants.php';
require_once 'Movie.php';

class Mysql {
	private $conn;

	function __construct() {
		$this->conn = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_NAME) or die('There was a problem connecting to the database,');
	}

	function verify_Username_and_Pass($un, $pwd) {

		$query = "SELECT id FROM users WHERE username =? AND password =? LIMIT 1";

		if($stmt = $this->conn->prepare($query)) {
			$stmt->bind_param('ss', $un, $pwd);
            $stmt->bind_result($id);
			$stmt->execute();
			if($stmt->fetch()) {
                setcookie("userid", $id, time()+64800);
				$stmt->close();
				return true;
			}
		}
	}

	function add_User($un, $pwd) {
        $query_add = "INSERT INTO `membership`.`users` (`username`, `password`) values(?, ?)";
        
		if($stmt2 = $this->conn->prepare($query_add)) {
            $stmt2->bind_param('ss', $un, $pwd);
			$stmt2->execute();
            $stmt2->close();
            return true;
        }
	
	}
    
    function updatePassword($uid, $pwd) {
        
        $query_update = "UPDATE `membership`.`users` SET password =? WHERE id =?" ;
        
        if($stmt3 = $this->conn->prepare($query_update)) {
            $stmt3->bind_param('ss', $uid, $pwd);
			$stmt3->execute();
            $stmt3->close();
            return true;
        }
    }
    
    function insertActivity($uid, $path){
        $query_insertActivity = "INSERT INTO `membership`.`activity` (`userid`, `path`) values(?, ?)";
        
		if($stmt4 = $this->conn->prepare($query_insertActivity)) {
            $stmt4->bind_param('ss', $uid, $path);
			$stmt4->execute();
            $stmt4->close();
            return true;
        }
    }
    
    function checkWatched($uid, $path){
            
            $query_watched = "SELECT * FROM activity WHERE userid =? AND path =?";
            
            if($stmt5 = $this->conn->prepare($query_watched)) {
                $stmt5->bind_param('ss', $uid, $path);
                $stmt5->execute();
                if($stmt5->fetch()) {
                    $stmt5->close();
                    return true;
                } else {
                    $stmt5->close();
                    return false;
                }
            }
    }
    
    function getMoviesByName()
    {
        $sql = "SELECT * FROM movie";
        $result = $this->conn->query($sql);
        return $this->getMovieData($result);
    }
    
    function getMoviesByYear()
    {
        $sql = "SELECT * FROM `movie` ORDER BY `movie`.`Year`  DESC";
        $result = $this->conn->query($sql);
        return $this->getMovieData($result);
    }
    
    function getMoviesByRating()
    {
        $sql = "SELECT * FROM `movie` ORDER BY `movie`.`imdbRating`  DESC";
        $result = $this->conn->query($sql);
        return $this->getMovieData($result);
    }
    
    function getMovieData($result)
    {
        $array = array();
        if ($result->num_rows > 0) {
            while($row = $result->fetch_assoc()) {
                $movie = new Movie();
                $movie->path = $row["path"];
                $movie->Title = $row["Title"];
                $movie->Year = $row["Year"];
                $movie->Rated = $row["Rated"];
                $movie->Released = $row["Released"];
                $movie->Runtime = $row["Runtime"];
                $movie->Genre = $row["Genre"];
                $movie->Director = $row["Director"];
                $movie->Writer = $row["Writer"];
                $movie->Actors = $row["Actors"];
                $movie->Plot = $row["Plot"];
                $movie->Language = $row["Language"];
                $movie->Country = $row["Country"];
                $movie->Awards = $row["Awards"];
                $movie->Poster = $row["Poster"];
                $movie->Metascore = $row["Metascore"];
                $movie->imdbRating = $row["imdbRating"];
                $movie->imdbVotes = $row["imdbVotes"];
                $movie->imdbID = $row["imdbID"];
                $movie->Type = $row["Type"];
                if(isset($movie))
                    $array[] = $movie;
            }
            $result->close();
        } else {
            $result->close();
            echo "0 results";
        }
        return $array;
    }
    
    function movieExist($movie)
    {
        $path = $movie->path;
        $query = "SELECT path FROM movie WHERE path = '".$path."' LIMIT 1";
        if(!($stmt6 = $this->conn->prepare($query))) {
            echo "Prepare failed movieExist(): (" . $this->conn->errno . ") " . $this->conn->error;
        }
        if (!$stmt6->execute()) {
            echo "Execute failed movieExist(): (" . $stmt->errno . ") " . $stmt->error;
        }
        if($stmt6->fetch()) {
            $stmt6->close();
            return true;
        }
        $stmt6->close();
        return false;
    }
    
    function updateMovie($movie)
    {
        $error = "";
        $query_update = "UPDATE `movie` SET `path`=?,`Title`=?,`Year`=?,`Rated`=?,`Released`=?,`Runtime`=?,`Genre`=?,`Director`=?,`Writer`=?,`Actors`=?,`Plot`=?,`Language`=?,`Country`=?,`Awards`=?,`Poster`=?,`Metascore`=?,`imdbRating`=?,`imdbVotes`=?,`imdbID`=?,`Type`=? WHERE `path` = ?";
        
        if(!($stmt7 = $this->conn->prepare($query_update))) {
            $error = "Prepare failed: (" . $this->conn->errno . ") " . $this->conn->error . "\r\n";
        }
        
        if (!($stmt7->bind_param("ssissssssssssssssssss", $movie->path, $movie->Title, $movie->Year, $movie->Rated, $movie->Released, $movie->Runtime, $movie->Genre, $movie->Director, $movie->Writer, $movie->Actors, $movie->Plot, $movie->Language, $movie->Country, $movie->Awards, $movie->Poster, $movie->Metascore, $movie->imdbRating, $movie->imdbVotes, $movie->imdbID, $movie->Type, $movie->path))) {
            $error += "Binding parameters failed: (" . $stmt8->errno . ") " . $stmt8->error;
        }
        
        if (!($stmt7->execute())) {
            $error += "Execute failed: (" . $stmt8->errno . ") " . $stmt8->error;
        }
        
        $stmt7->close();
        return $error;
    }
    
    function insertMovie($movie)
    {
        $error = "";
        $query_update = "INSERT INTO `movie`(`path`, `Title`, `Year`, `Rated`, `Released`, `Runtime`, `Genre`, `Director`, `Writer`, `Actors`, `Plot`, `Language`, `Country`, `Awards`, `Poster`, `Metascore`, `imdbRating`, `imdbVotes`, `imdbID`, `Type`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        if(!($stmt8 = $this->conn->prepare($query_update))) {
            $error += "Prepare failed: (" . $this->conn->errno . ") " . $this->conn->error . "\r\n";
        }

        if (!($stmt8->bind_param("ssisssssssssssssssss", $movie->path, $movie->Title, $movie->Year, $movie->Rated, $movie->Released, $movie->Runtime, $movie->Genre, $movie->Director, $movie->Writer, $movie->Actors, $movie->Plot, $movie->Language, $movie->Country, $movie->Awards, $movie->Poster, $movie->Metascore, $movie->imdbRating, $movie->imdbVotes, $movie->imdbID, $movie->Type))) {
            $error += "Binding parameters failed: (" . $stmt8->errno . ") " . $stmt8->error;
        }
        
        if (!($stmt8->execute())) {
            $error += "Execute failed: (" . $stmt8->errno . ") " . $stmt8->error;
        }
        
        $stmt8->close();
        return $error;
    }
    
    function updateDatabase($movie)
    {
        if($this->movieExist($movie))
        {
            $this->updateMovie($movie);
            return true;
        }
        else
        {
            $this->insertMovie($movie);
            return false;
        }
    }
    
    function searchDatabase($name)
    {
        $array = array();
        $query_search = "SELECT * FROM `membership`.`movie` WHERE Title LIKE =?";
        echo "wtf";
        echo "\r\n";
        if($stmt9 = $this->conn->prepare($query_search)) {
            $stmt9->execute();
            $stmt9->close();
            return true;
        }
        return $array;
    }
}
  
