<?php

namespace App\Http\Controllers;
use Illuminate\Foundation\Auth\Access\AuthorizesRequests;
use Illuminate\Foundation\Bus\DispatchesJobs;
use Illuminate\Foundation\Validation\ValidatesRequests;
use Illuminate\Http\Request;
use Illuminate\Routing\Controller as BaseController;

class Controller extends BaseController
{
    use AuthorizesRequests, DispatchesJobs, ValidatesRequests;

    public function __construct(){
        $header = apache_request_headers();
        $this->language = isset($header['languageId']) ? $header['languageId'] : 'en';
    }

    function do_in_background($url,$params_data)
    {
        $parts=parse_url($url);
        $post_string = http_build_query($params_data);
        $fp = fsockopen($parts['host'], isset($parts['port'])?$parts['port']:80, $errno, $errstr, 30);
        if (!$fp) {
            return false;
        } else {
            $out = "POST ".$parts['path']." HTTP/1.1\r\n";
            $out.= "Host: ".$parts['host']."\r\n";
            $out.= "Content-Type: application/x-www-form-urlencoded\r\n";
            $out.= "Content-Length: ".strlen($post_string)."\r\n";
            $out.= "Connection: Close\r\n\r\n";
            if (isset($post_string)) $out.= $post_string;
            fwrite($fp, $out);
            fclose($fp);
            return true;
        }
    }
    
}
