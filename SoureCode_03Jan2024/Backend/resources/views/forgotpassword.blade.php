<!DOCTYPE html> 
<html>
   <head>
      <title>{{$data['title']}}</title>
   </head>
   <body>
<div style="width:100% !important; float: left !important; text-align: center !important;">
    <div style="width:100% !important; max-width: 600px !important; display: inline-block !important; padding:15px !important;">
    <table cellpadding="0" cellspacing="0" width="100%">
        <tbody>
            <tr>
                <td>
                    <div class='hedaerLogo' style='width:100% !important; float: left !important;background: #065E9D !important;padding: 10px !important; text-align: center !important;'>
                        <img src="{{ isset($data['logo_url']) ? $data['logo_url'] : '' }}" alt='logo' class='img-fluid' style='width: 90px !important;     margin: 0 !important; object-fit: contain !important;'>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                     <div class='terms-conditions-page justify-text' style='font-family: Open Sans, sans-serif; font-size: 15px;width: 90% !important; margin: auto !important;'>
                        <p style='font-size: 25px;font-weight: 600;font-family: Open Sans, sans-serif; color: #065E9D !important;'><strong>Forgot Password</strong></p>
                        <p style='font-size: 15px;'>Dear User,</p>
                        <p style='font-size: 15px;'>To reset your password, please visit the following page: <a href ="{{$data['url']}}" style='text-decoration: none; color:#0014ff !important;'><b>Click Here</b></a></p>
                        
                        <p style='font-size: 15px;'>Thank You</p>
                        <p style='font-size: 15px;'>Rec'd Team</p>
                    </div>      
                </td>
            </tr>
        </tbody>
    </table>
    </div>
 </div> 
   </body>
</html>