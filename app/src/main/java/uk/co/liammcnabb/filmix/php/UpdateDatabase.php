<?php
    require_once 'Movie.php';
    require_once 'Mysql.php';

    define('SHOW_PATH', TRUE);
    define('SHOW_PARENT_LINK', FALSE);
    define('SHOW_HIDDEN_ENTRIES', FALSE);
    
    class UpdateDatabase{
        
        private $database;
        
        public function UpdateDatabase()
        {
            $this->database = new Mysql();
        }
        
        function printMessage($message)
        {
            echo "=============================================== \r\n";
            echo $message . "\r\n";
            echo "=============================================== \r\n\r\n";
        }
        
        function get_grouped_entries($path) {
            list($dirs, $files) = $this->collect_directories_and_files($path);
            $dirs = $this->filter_directories($dirs);
            $files = $this->filter_files($files);
            return array_merge(
                               array_fill_keys($dirs, TRUE),
                               array_fill_keys($files, FALSE));
        }
        
        function collect_directories_and_files($path) {
            $entries = scandir($path);
            return $this->array_partition($entries, function($entry) {
                                   return is_dir($entry);
                                   });
        }
        
        function array_partition($array, $predicate_callback) {
            $results = array_fill_keys(array(1, 0), array());
            foreach ($array as $element) {
                array_push(
                           $results[(int) $predicate_callback($element)],
                           $element);
            }
            return array($results[1], $results[0]);
        }
        
        function filter_directories($dirs) {
            return array_filter($dirs, function($dir) {
                                return $dir != '.'
                                && (SHOW_PARENT_LINK || $dir != '..')
                                && !is_hidden($dir);
                                });
        }
        
        function filter_files($files) {
            return array_filter($files, function($file) {
                                return !$this->is_hidden($file)
                                && substr($file, -4) != '.php';
                                });
        }
        
        function is_hidden($entry) {
            return !SHOW_HIDDEN_ENTRIES
            && substr($entry, 0, 1) == '.';
        }
        
        function endsWith($haystack, $needle)
        {
            return $needle === "" || substr($haystack, -strlen($needle)) === $needle;
        }
        
        function cleanFile($name) {
            for($x =1932; $x < 2020; $x++)
            {
                $name = str_replace((string)$x,'',$name);
            }
            $name = trim(str_replace("____", "'",str_replace('EXTENDED.CUT','',str_replace('10th Anniversary Edition','',str_replace('10800p','',str_replace('UNRATED','',str_replace('anoXmous_','',str_replace('Bluray','',str_replace('264','',str_replace('EXTENDED','',str_replace('GAZ','',str_replace('BDRip','',str_replace('AAC','',str_replace('EXTENDED CUT','',str_replace('BOKUTOX','',str_replace('REMASTERED','',str_replace('-FoV','',str_replace('XviD','',str_replace('XviD-REWARD','',str_replace('RETAIL','',str_replace('xvid-reward','',str_replace('dvdrip','',str_replace('XviD-SAiNTS','',str_replace('DVDRip','',str_replace('bluray','',str_replace('repack','',str_replace('-geckos','',str_replace('BRrip','',str_replace('BrRip','',str_replace('BluRay','',str_replace('x264','',str_replace('mp4','',str_replace('YIFY','',str_replace('1080p','',str_replace('720p','',str_replace('.',' ',$name))))))))))))))))))))))))))))))))))));
            str_replace('Crazy, Stupid, Love', 'Crazy, Stupid, Love.', $name);
            str_replace('vs', 'vs.', $name);
            str_replace('X-Men', 'X-Men:', $name);
            str_replace("____", "'", $name);
            return $name;
        }
        
        function serverRequest($url)
        {
            // create curl resource
            $ch = curl_init();
            
            // set url
            curl_setopt($ch, CURLOPT_URL, $url);
            
            //return the transfer as a string
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
            
            // $output contains the output string
            $output = curl_exec($ch);
            
            // close curl resource to free up system resources
            curl_close($ch);
            return $output;
        }
        
        function parseJSON($jsonString)
        {
            $json = json_decode($jsonString);
            $movie = new Movie();
            $movie->Response = false;
            foreach ($json as $key => $value)
            {
              if($key == "Title")
                  $movie->Title = $value;
               else if($key == "Year")
                        $movie->Year = $value;
               else if($key == "Rated")
                       $movie->Rated = $value;
               else if($key == "Released")
                       $movie->Released = $value;
               else if($key == "Runtime")
                       $movie->Runtime = $value;
               else if($key == "Genre")
                       $movie->Genre = $value;
               else if($key == "Director")
                       $movie->Director = $value;
               else if($key == "Writer")
                       $movie->Writer = $value;
               else if($key == "Actors")
                       $movie->Actors = $value;
               else if($key == "Plot")
                       $movie->Plot = $value;
               else if($key == "Language")
                       $movie->Language = $value;
               else if($key == "Country")
                       $movie->Country = $value;
               else if($key == "Awards")
                       $movie->Awards = $value;
               else if($key == "Poster")
                       $movie->Poster = $value;
               else if($key == "Metascore")
                       $movie->Metascore = $value;
               else if($key == "imdbRating")
                       $movie->imdbRating = $value;
               else if($key == "imdbVotes")
                       $movie->imdbVotes = $value;
               else if($key == "imdbID")
                       $movie->imdbID = $value;
               else if($key == "Type")
                       $movie->Type = $value;
               else if($key == "Response")
                       $movie->Response = $value;
            }
            return $movie;
        }
        
        function createURL($fileName)
        {
            $fileName = str_replace(' ', '+', $fileName);
            return 'http://www.omdbapi.com/?t=' . htmlspecialchars($fileName) . '&y=&plot=short&r=json&type=movie';
        }
        
        function updateDirectory($path)
        {
            $listFiles = $this->scanDir($path);
            $this->beginUpdate($listFiles);
        }
        
        function insertMovies($path)
        {
            $listFiles = $this->sortExistingFiles($this->scanDir($path));
            $this->beginUpdate($listFiles);
        }
        
        function beginUpdate($listFiles)
        {
            $listMovies = $this->createMovies($listFiles);
            $this->saveImages($listMovies);
            $listUpdated = $this->commitToDatabase($listMovies);
            $finishedList = $this->validateDatabase($listMovies);
            $failedCount = sizeof($listFiles) - sizeof($finishedList);
            $insertedCount =sizeof($listMovies) - sizeof($listUpdated);
            
            $this->printMessage("Failed (".$failedCount.")." . "\r\n" . "Inserted (".$insertedCount. ")." . "\r\n" . "Updated (".sizeof($listUpdated). ")." . "\r\n" . "Complete (".sizeof($finishedList). ").");
        }
        
        function createMovies($listFiles)
        {
            $i = 0;
            $array = array();
            if(sizeof($listFiles) > 0)
            {
                $this->printMessage("Contacting IMDB Database (". sizeof($listFiles). ").");
                $this->show_status($i, sizeof($listFiles));
                foreach($listFiles as $file)
                {
                    $fileName = $this->cleanFile(end(explode("/",$file)));
                    $requestURL = $this->createURL($fileName);
                    $json = $this->serverRequest($requestURL);
                    $movie = $this->parseJSON($json);
                    $movie->path = $file;
                    if($movie->Response == 'True')
                    {
                        $array[] = $movie;
                    }
                    else
                    {
                        echo $fileName . "\r\n";
                    }
                    $i++;
                    $this->show_status($i, sizeof($listFiles));
                    
                }
            }
            return $array;
        }
        
        function commitToDatabase($listMovies)
        {
            $i = 0;
            $array = array();
            if(sizeof($listMovies) > 0)
            {
                $this->printMessage("Commiting to local database (".sizeof($listMovies).").");
                $this->show_status($i, sizeof($listMovies));
                foreach($listMovies as $movie)
                {
                    if($this->database->updateDatabase($movie))
                    {
                        $array[] = $movie;
                    }
                    $i++;
                    $this->show_status($i, sizeof($listMovies));
                }
            }
            return $array;
        }
        
        function validateDatabase($listMovies)
        {
            $array = array();
            if(sizeof($listMovies) > 0)
            {
                $this->printMessage("Validating Database (".sizeof($listMovies).").");
                foreach($listMovies as $movie)
                {
                    if($this->database->movieExist($movie))
                    {
                        $array[] = $movie;
                    }
                }
            }
            return $array;
        }
        
        function sortExistingFiles($listFiles)
        {
            $missingFiles = array();
            $existingFiles = array();
            if(sizeof($listFiles) > 0)
            {
                $movies = $this->database->getMoviesByName();
                foreach($movies as $movie)
                {
                    $existingFiles[] = $movie->path;
                }
                foreach($listFiles as $file)
                {
                    if (!(in_array($file, $existingFiles)))
                    {
                        $missingFiles[] = $file;
                    }
                }
            }
            return $missingFiles;
        }
        
        function saveImages($listMovies)
        {
            $i = 0;
            if(sizeof($listMovies) > 0)
            {
                $existingIMG = array();
                $entries = $this->get_grouped_entries('/var/www/imgMovies');
                foreach ($entries as $entry => $is_dir)
                {
                    $class_name = $is_dir ? 'directory' : 'file';
                    $escaped_entry = htmlspecialchars($entry);
                    if($this->endsWith($escaped_entry, ".jpg")) {
                        $existingIMG[] = 'imgMovies/' . $escaped_entry;
                    }
                }
                
                $this->printMessage("Saving Images (".sizeof($listMovies).").");
                $this->show_status($i, sizeof($listMovies));
                //shell_exec('sudo rm /var/www/imgMovies/*.jpg');
                foreach($listMovies as $movie)
                {
                    $list = (explode("/",$movie->Poster));
                    if(count($list) > 2)
                    {
                        $domain = 'http://' . $list[2];
                        $cmd = 'sudo ./saveImage.sh ' . $domain . ' ' . $movie->Poster;
                        shell_exec ( 'sudo wget --referer=' . $domain . ' ' . $movie->Poster. ' -P /var/www/imgMovies > /dev/null 2>&1' );
                        $movie->Poster = 'imgMovies/' . end($list);
                        if(!(in_array($movie->Poster, $existingIMG)))
                        {
                            $imgLocation = '/var/www/' .$movie->Poster;
                            shell_exec('sudo convert -verbose -resize x300 ' . $imgLocation . ' ' . $imgLocation . ' > /dev/null 2>&1');
                        }
                    }
                    else
                    {
                        $movie->Poster = 'imgMovies/blank.jpg';
                    }
                    $i++;
                    $this->show_status($i, sizeof($listMovies));
                }
            }
        }
        
        function scanDir($path)
        {
            $array = array();
            $entries = $this->get_grouped_entries($path);
            foreach ($entries as $entry => $is_dir)
            {
                $class_name = $is_dir ? 'directory' : 'file';
                $escaped_entry = htmlspecialchars($entry);
                if($this->endsWith($escaped_entry, ".mp4")) {
                    $array[] = $path . '/' . $escaped_entry;
                }
                else {
                    $subPath = ($path . '/' . $entry);
                    $listMovies = $this->scanDir($subPath);
                    foreach($listMovies as $movie)
                    {
                        $array[] = $movie;
                    }
                }
            }
            return $array;
        }
        
        function show_status($done, $total, $size=30) {
            
            static $start_time;
            
            // if we go over our bound, just ignore it
            if($done > $total) return;
            
            if(empty($start_time)) $start_time=time();
            $now = time();
            
            $perc=(double)($done/$total);
            
            $bar=floor($perc*$size);
            
            $status_bar="\r[";
            $status_bar.=str_repeat("=", $bar);
            if($bar<$size){
                $status_bar.=">";
                $status_bar.=str_repeat(" ", $size-$bar);
            } else {
                $status_bar.="=";
            }
            
            $disp=number_format($perc*100, 0);
            
            $status_bar.="] $disp%  $done/$total";
            
            $rate = 0;
            if($done != 0)
            {
                $rate = ($now-$start_time)/$done;
            }
            $left = $total - $done;
            $eta = round($rate * $left, 2);
            
            $elapsed = $now - $start_time;
            
            //$status_bar.= " remaining: ".number_format($eta)." sec.  elapsed: ".number_format($elapsed)." sec.";
            
            echo "$status_bar  ";
            
            flush();
            
            // when done, send a newline
            if($done == $total) {
                echo "\r\n\r\n";
            }
            
        }
    }