
class SessionUser {

 constructor(firstName,lastName,sessionId)
 {
 this.firstName =firstName;
 this.lastName = lastName;
 this.sessionId = sessionId

 }
}

sessionUser = new SessionUser('','','')
 //url = "https://localhost:20452/stockapi/";
 let completeURL = window.location.href;
 let index = completeURL.indexOf("/bol");
 url = completeURL.substring(0,index) + '/stockapi/';

//--disable-web-security --disable-gpu --user-data-dir=~/chromeTem
function closePopup()
{
             $("#userFieldsDiv").css("display","flex");
             $("#otpEntryDiv").css("display","none");
             $("#newUserMode").removeClass('modal show');
             $("#newUserMode").addClass('modal fade');
             $("#newUserMode").css("display","none");
             $("#forgotPWD").removeClass('modal show');
             $("#forgotPWD").addClass('modal fade');
             $("#forgotPWD").css("display","none");

             $("#userFieldsDiv").removeClass('modal show');
             $("#userFieldsDiv").addClass('modal fade');
             $("#userFieldsDiv").css("display","none");

             $("#divTermsCond").removeClass('modal fade');
             $("#divTermsCond").addClass('modal show')
             $("#divTermsCond").css("display","flex");



}
function searchAndNavStock()
{
    let securityName =  $("#txtSecurty").val();
    console.log('selected ' + securityName);
    var requestStr = url + "uiapi/getStockFromSecurity?securityName=" + securityName ;
    var reqObject = new XMLHttpRequest();
    reqObject.open("GET",requestStr,false);
    reqObject.send();
    console.log("Resp" + reqObject.responseText);
    var snapsotresponse  =   JSON.parse(reqObject.responseText)  ;
    let bseCode = snapsotresponse.bseCode;
    let pageId= './stockDetails.html?bseCode=' + bseCode;
    $("#frmCont").attr('src',pageId);
    return false;



}

function getLookupWithAjax( currentCtrl,dataListCtrlName)
 {
     let unicode = event.keyCode;
     if (unicode == 13) {
        searchAndNavStock();
        return ;
     }

    console.log('reached' + unicode);
     var srValue = currentCtrl.value ;
   	if(srValue.length  > 2 || srValue.indexOf('*') !=  -1 ) {

     var lookupType ='Stock' ;
 	currentCtrl.autocomplete ="on";

 	var requestStr = url + "uiapi/lookup?lookupType=" + lookupType
 	+ "&searchStr=" + srValue ;

 	var reqObject = new XMLHttpRequest();
 	reqObject.open("GET",requestStr,false);
 	reqObject.send();
 	console.log("Resp" + reqObject.responseText);
 	var elem = document.getElementsByName(dataListCtrlName)[0];
 	console.log ('before' + elem.innerHTML) ;
  	elem.innerHTML='';
 	 var options = '';
 	var propArray =  JSON.parse(reqObject.responseText) ;
    var found =false;
 	for (var key in propArray) {
 		  var value = propArray[key];
 		  options += '<option  value="'+value+'" />' + key + '</option>';
 		  found =true ;
 	}
 	if (found == false) {
 		currentCtrl.autocomplete ="off";
 	}
 	elem.innerHTML=  options;
 	console.log ('after' + elem.innerHTML) ;
 	}

 }

function resendPassword()
{
  let email = $("#txtForgottenEmail").val();
let request = new XMLHttpRequest () ;
   request.open("GET",url+'user/sendPassword?userEmail='+email,true);
        request.onreadystatechange = function() {
              if (this.readyState == 4 && this.status == 200) {
                   console.log("send and received");
                   $("#infomessage").html("Password Emailed!");
                   closePopup();
              }else
              {
               $("#errorEmailEntryText").html("Could not send email");
              }
          };

          try {
            request.send(true) ;
          }catch(err)
          {
           $("#errorEmailEntryText").html("email not send");
          }

}

function updatePassword()
{
  let request = new XMLHttpRequest () ;
  let pass = $("#txtPass").val();
  let confirmPass = $("#txtConfPass").val();
  if ( pass.trim() == '' || confirmPass.trim() == '' )
    {
    console.log('error') ;
     $("#errorText").css("color","red");
     $("#errorText").html('<br>Please enter Password');
     return ;
     }
     if( pass != confirmPass) {
     $("#errorText").css("color","red");
       $("#errorText").html('<br> Password does not match');
        return ;
     }
    $("#errorText").html('');
     $("#errorText").css("color","blue");


  let input = btoa(pass)
  request.open("GET",url+'user/resetPassword?updatedPWD=' + input,true);
  request.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
           console.log("Response =" + request.responseText )
           var snapsotresponse  =   JSON.parse(request.responseText)  ;
           alert('Password Updated, Please relogin');
           let fullurl = url + 'api/logout';
           let request2 = formRequest("GET",fullurl,false);
           request2.send() ;
           console.log("Response =" + request2.responseText );
           window.parent.location.replace('./bologin.html');
           }
   };

   request.send(true);

}

function updatePref()
{


  let postContent= { };
  let dDays = $("#txtDBoard").val();
  let validationAlgo = $("#vltTCQ").val();
  let landingPage = $("#landingPage").val();
  let techDays = $("#techDays").val();
  postContent['landingPage'] = landingPage;
  postContent['dashboardDays'] = dDays;
  postContent['validationAlgo'] = validationAlgo;
  postContent['techDays'] = techDays;

  let request = formRequest("POST",url+'user/updateUserPref');
  setToken(request);
  request.send(JSON.stringify(postContent),true) ;
  $("#errorText").html('');
  $("#errorText").css("color","blue");
  $("#errorText").html('Preferences updated');

}
function updateName()
{
  let request = new XMLHttpRequest () ;
  let firstName = $("#txtFname").val();
  let lastName = $("#txtLname").val();
  if ( firstName.trim() == '' || lastName.trim() == '' )
    {
    console.log('error') ;
     $("#errorText").css("color","red");
     $("#errorText").html('<br>Please enter First Name and Last Name');
     return ;
     }
    $("#errorText").html('');
     $("#errorText").css("color","blue");

  request.open("GET",url+'user/updateNames?firstName=' + firstName + '&lastName=' + lastName,true);
  request.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
           console.log("Response =" + request.responseText );
           var snapsotresponse  =   JSON.parse(request.responseText)  ;
           $("#errorText").html('Name updated');
           }
   };
   request.send(true);

}
function getLandingPage()
{
let request = new XMLHttpRequest () ;
  request.open("GET",url+'user/getUserPref',false);
   request.send(false);
   var snapsotresponse  =   JSON.parse(request.responseText)  ;
    return snapsotresponse.landingPage;
}
function getUserPref()
{
  let request = new XMLHttpRequest () ;
  request.open("GET",url+'user/getUserPref',true);
  request.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
           console.log("Response =" + request.responseText )
           var snapsotresponse  =   JSON.parse(request.responseText)  ;
           $("#txtDBoard").val(snapsotresponse.dashboardDays);
           $("#vltTCQ").val(snapsotresponse.validationAlgo);
           $("#landingPage").val(snapsotresponse.landingPage);
      }
  };
   request.send(true);
}

function getUserDetails()
{
  let request = new XMLHttpRequest () ;
  request.open("GET",url+'user/getLoggedinUser',true);

  request.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
           console.log("Response =" + request.responseText )
           var snapsotresponse  =   JSON.parse(request.responseText)  ;
           $("#txtFname").val(snapsotresponse.firstName);
           $("#txtLname").val(snapsotresponse.lastName);
           $("#txtEmail").val(snapsotresponse.emailId);
           $("#txtPhone").val(snapsotresponse.phoneNumber);
           }
  };
   request.send(true);

}

function submitOTP()
{
  let phone = $("#txtPhone").val();
  let email = $("#txtEmail").val();
  let fName = $("#txtFName").val();
  let lName = $("#txtLName").val();
  let pass = $("#txtPass").val();
  let otp = $("#txpOTP").val();
  let screenname = $("#txtScreenName").val();
  let lstInvAm = $("#lstInvAm").val();
   let strategy = $("#lstInvestStrat").val();
  var data = {
          "emailId" : email,
          "firstName" : fName,
          "lastName": lName,
          "password":pass,
          "phoneNumber": phone,
          "otp":otp,
          "screenName":screenname,
          "investRange" : lstInvAm,
          "investStrategy":strategy

      };
      let request = new XMLHttpRequest () ;
      console.log(url+'user/setOTP');
      let inp = JSON.stringify(data);
      console.log(inp);
      let inpBtoA= btoa(inp);
      console.log(inpBtoA);
      request.open("GET",url+'user/setOTP?inputData='+inpBtoA,true);
      request.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                 console.log("send and received");
                 $("#userFieldsDiv").css("display","flex");
                 $("#otpEntryDiv").css("display","none");
                  $("#newUserMode").removeClass('modal show');
                 $("#newUserMode").addClass('modal fade');
                  $("#newUserMode").css("display","none");
                   $("#infomessage").html("User Created!");
            }else
            {
             $("#errorSecText").html("OTP is not matching");
            }
        };

        try {
          request.send(true) ;
           console.log('user creation with OTP=' + JSON.stringify(data) );
        }catch(err)
        {
         $("#errorSecText").html("OTP is not matching");
        }


}

function sendOTP()
{

  let phone = $("#txtPhone").val();
  let email = $("#txtEmail").val();
  let fName = $("#txtFName").val();
  let lName = $("#txtLName").val();
  let pass = $("#txtPass").val();
  let confirmPass = $("#txtConfPass").val();
   let screenname = $("#txtScreenName").val();

  if (phone.trim() =='' || email.trim() == '' || fName.trim() == '' || lName.trim() =='' || pass.trim() == '' || confirmPass.trim() == '' )
  {
  console.log('error') ;
   $("#errorText").html('<br>Please enter all fields');
   return ;
   }
   if( pass != confirmPass) {
     $("#errorText").html('<br> Password does not match');
      return ;
   }
  $("#errorText").html('');

  let request = new XMLHttpRequest () ;
  request.open("GET",url+'user/saveOTP?phoneNumber=' + phone +"&email="+ email +"&screenName=" + screenname,false);
  try {
       request.send(false) ;
       var snresponse = JSON.parse(request.responseText);
       if (snresponse['Result'] == 'Success') {
            $("#userFieldsDiv").css("display","none");
             $("#txpOTP").val(9091);
                    submitOTP();
         //   $("#otpEntryDiv").css("display","block");
       }else  {
              $("#errorText").html(snresponse['message']);
       }



    }catch(err)
    {
     $("#errorText").html("Could not send OTP");
    }

  /*request.onreadystatechange = function() {
      if (this.readyState == 4 && this.status == 200) {
           $("#userFieldsDiv").css("display","none");
           $("#otpEntryDiv").css("display","block");

      }else
      {
       var response = Json.parse(request.responseText);
       $("#errorText").html(response['message']);
      }
  };*/



}

function forgotPasswordPopup()
{

console.log('open forgot pwd dialog');
 $("#forgotPWD").removeClass('modal fade');
 $("#forgotPWD").addClass('modal show');
 $("#forgotPWD").css("display","block");

}

function agreeDisagree()
{
   let clicked =  $("#chkTermsAgreed").prop("checked");
   if (clicked == true ) {
     $("#btnAgreeTerms").prop("disabled",false);
   }else {
    $("#btnAgreeTerms").prop("disabled",true);
   }

}

function termsAndConditions()
{

console.log('open dialog');
 $("#newUserMode").removeClass('modal fade');
 $("#newUserMode").addClass('modal show');
 $("#newUserMode").css("display","block");
}

function openNewUserpopup()
{


$("#divTermsCond").removeClass('modal show');
 $("#divTermsCond").addClass('modal fade');
 $("#divTermsCond").css("display","none");

console.log('open dialog');
 $("#userFieldsDiv").removeClass('modal fade');
 $("#userFieldsDiv").addClass('modal show');
 $("#userFieldsDiv").css("display","block");
}
function formRequest(methodType,api)
{
      let request = new XMLHttpRequest () ;
      request.open(methodType,api,false);
      request.setRequestHeader("Content-type", "application/json");
      request.setRequestHeader("Access-Control-Allow-Origin", url);
      return request ;
}


function getMenus()
{
     let request = formRequest("GET",url+'api/application/getMenus');
     setToken(request);
     request.send() ;
     var snapsotresponse  =   JSON.parse(request.responseText)  ;
     console.log("Responss   e =" + request.responseText );
     return  snapsotresponse;
}
function getGenericList(pageid,currentPage,recordsPerPage)
{
    console.log(pageid);
    var from = currentPage * recordsPerPage;
    var to= from  + recordsPerPage;
    let request = formRequest("GET",url+'api/commonui/getPage?pageid='+ pageid + "&from=" + from + "&to=" + to);
    setToken(request);
    request.send() ;
    var snapsotresponse  =   JSON.parse(request.responseText)  ;
    console.log("Responss   e =" + request.responseText );
     return  snapsotresponse;

}

function getPageCreate(pageid)
{
    let request = formRequest("GET",url+'api/commonui/getPage?pageid='+ pageid);
    setToken(request);
    request.send() ;
    var snapsotresponse  =   JSON.parse(request.responseText)  ;
    console.log("Response =" + request.responseText );
    return  snapsotresponse;

}
function changeDateformat(edate)
{
    var newdate = edate.split("-").reverse().join("-");
    console.log(newdate);
     return newdate;
}
function populateData(pkValue,entity)
{

    fullurl = url + 'api/commonui/getDataFromPK?entity=' + entity + "&pk=" + pkValue;
    let request = formRequest("GET",fullurl,false);
    request.send() ;
    var snapsotresponse  =   JSON.parse(request.responseText)  ;
    console.log("Response =" + request.responseText );

    ct = $("#frmgenericEdit")[0].elements.length ;
    let postContent= { };
    for (i =0 ; i < ct ;i ++)
    {
       var edCtrl = $("#frmgenericEdit")[0].elements[i];
       var jsonTag = edCtrl.getAttribute("data-json");
       console.log('edCtrl.type=' + edCtrl.type);
       if (jsonTag != '' && jsonTag != null )
       {
            if(jsonTag.includes("."))
            {
                parts = jsonTag.split(".");
                console.log(snapsotresponse[parts[0]][parts[1]]);
                edCtrl.value=snapsotresponse[parts[0]][parts[1]];

            } else if (edCtrl.type == 'date' ||  edCtrl.type == 'datetime-local') {
               edCtrl.value =  snapsotresponse[jsonTag];
            }else if (edCtrl.type == 'checkbox')
            {
                if (snapsotresponse[jsonTag] == true )
                    edCtrl.checked = true;
                else
                    edCtrl.checked = false;

            }else
            {
                edCtrl.value= snapsotresponse[jsonTag];
            }
       }


    }
//    alert('done');

}
function setToken (request)
{

    var allcookies = document.cookie;
   cookiearray = allcookies.split(';');

   for(var i=0; i<cookiearray.length; i++) {
      name = cookiearray[i].split('=')[0];
      value = cookiearray[i].split('=')[1];
     console.log ("Key is : " + name + " and Value is : " + value);
      if(name.trim() =='XSRF-TOKEN') {
          request.setRequestHeader("X-XSRF-TOKEN", value);
          console.log('added x-xsrf-token');
          }
   }
}

function clearFilter( pageid,recordsPerPage,tableId,pkField)
{
    ct = $("#frmFilter")[0].elements.length ;
    let postContent= { };

    for (i =0 ; i < ct ;i ++)
    {
           var ctrl = $("#frmFilter")[0].elements[i];
            if(ctrl.type == 'checkbox')
           {
              ctrl.checked = false;
           }else
           {
                ctrl.value = ''
           }

    }
    $("#hdnAppliedFilter")[0].value = '';
    reloadListWithContent(pageid,0,recordsPerPage,tableId,pkField);
}

function applyFilter( pageid,recordsPerPage,tableId,pkField)
{
    ct = $("#frmFilter")[0].elements.length ;
    let postContent= { };

    for (i =0 ; i < ct ;i ++)
    {
           var ctrl = $("#frmFilter")[0].elements[i];
           var propValue = ctrl.value;
           var propTag = ctrl.getAttribute("data-json");
            if (propValue != '' && propValue != null )
            {
              if(ctrl.type == 'checkbox')
               {
                  if (ctrl.checked == true )
                     postContent[propTag] = 'true';
                  else
                    postContent[propTag] = 'false';
               }else
               {
               postContent[propTag] = propValue;
               }
            }
    }
    $("#hdnAppliedFilter")[0].value = JSON.stringify(postContent);
    console.log(postContent);

    reloadListWithContent(pageid,0,recordsPerPage,tableId,pkField);
}


function updateData(entity,event)
{
ct = $("#frmgenericEdit")[0].elements.length ;
let postContent= { };

for (i =0 ; i < ct ;i ++)
{
   var ctrl = $("#frmgenericEdit")[0].elements[i];
   var propValue = ctrl.value;
   var propTag = ctrl.getAttribute("data-json");
   setJsonValueForSave(postContent,propTag,propValue,ctrl);


}
console.log(postContent);
console.log(entity);
$('#editErrorDiv')[0].innerHTML = '';
$("#editErrorDiv").hide();
fullurl = url + 'api/generic/update?entity=' + entity ;
let request = formRequest("POST",fullurl);
setToken(request);

     request.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
        // alert('success');
        }else
        {
            var saveResponse  =   JSON.parse(request.responseText)  ;
            console.log(saveResponse);
            errors = saveResponse.errors ;
            for (var i in errors) {
                        var error = errors[i];
                        $('#editErrorDiv')[0].innerHTML = $('#editErrorDiv')[0].innerHTML + error.message  + "<br>" ;
            }
            event.preventDefault();
            event.stopPropagation();
            $("#editErrorDiv").show(100);

        }

    };

    try {
       request.send(JSON.stringify(postContent),true) ;
    }catch(err)
    {
     $("#erromessage")[0].innerHTML ="Authorization failed";
    }
return false;
}


function setJsonValueForSave(postContent,propTag,propValue,ctrl)
{
   if (propTag != '' && propTag != null )
   {
        if(propTag.includes("."))
        {
            parts = propTag.split(".");
            let  subObject = { };
            setJsonValueForSave (subObject,parts[1],propValue,ctrl);
            postContent[parts[0]]= subObject;
        }
        else
        {
             if(ctrl.type == 'checkbox')
             {
                if (ctrl.checked == true )
                   postContent[propTag]= 'true';
                else
                  postContent[propTag] = 'false';
             }else if (ctrl.type == 'datetime-local')
             {
                postContent[propTag] = propValue.replace('T',' ')
             }else
             {
                  postContent[propTag] = propValue;
             }
         }
   }

}

function saveData(entity,event)
{
ct = $("#frmgenericAdd")[0].elements.length ;
let postContent= { };
for (i =0 ; i < ct ;i ++)
{
      var ctrl = $("#frmgenericAdd")[0].elements[i];
      var propValue = ctrl.value;
      var propTag = ctrl.getAttribute("data-json");
      setJsonValueForSave(postContent,propTag,propValue,ctrl);

}
console.log(postContent);
console.log(entity);
$('#addErrorDiv')[0].innerHTML = '';
$("#addErrorDiv").hide();
fullurl = url + 'api/generic/create?entity=' + entity ;
let request = formRequest("POST",fullurl);
setToken(request);
     request.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
                return true;
        }else
        {
            var saveResponse  =   JSON.parse(request.responseText)  ;
            console.log(saveResponse);
            errors = saveResponse.errors ;
            for (var i in errors) {
                        var error = errors[i];
                        $('#addErrorDiv')[0].innerHTML = $('#addErrorDiv')[0].innerHTML + error.message  + "<br>" ;
            }
            event.preventDefault();
            event.stopPropagation();
            $("#addErrorDiv").show(100);
        }

    };

    try {
       request.send(JSON.stringify(postContent),true) ;
    }catch(err)
    {
     $("#erromessage")[0].innerHTML ="could not send message";
    }
return false;
}


function showPage(pageName)
{
 $("#ifrmContent")[0].src=pageName;
}
function logout()
{
    let fullurl = url + 'api/logout';
    let request = formRequest("GET",fullurl,false);
    request.send() ;
   // var snapsotresponse  =   JSON.parse(request.responseText)  ;
    console.log("Response =" + request.responseText );
    window.location.replace('./bologin.html');

}
function login()
    {
        var name = $("#txtname")[0].value ;
        var pwd =  $("#txtpwd")[0].value ;
    var data = {
        "userId" : name,
        "pwd" : pwd
    };
     let request = new XMLHttpRequest () ;
    auth = "Basic " + btoa(name + ":" + pwd);
    request.open("GET",url+'api/justTry',false);
    request.setRequestHeader("Authorization", auth);
    setToken(request);
     request.setRequestHeader("Content-type", "application/json");
       /* request.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
         window.location.href = './bolanding.html'
        }else
        {
         $("#erromessage")[0].innerHTML ="Authorization failed";
        }
    };
    */
    try {
       request.send(JSON.stringify(data),false) ;
       var snapsotresponse  =  JSON.parse( request.responseText)  ;
       console.log(snapsotresponse);
       if (snapsotresponse['result'] == 'success'){
            window.location.href = './bolanding.html'
       }else
       {
            $("#erromessage")[0].innerHTML ="Authorization failed";
       }

    }catch(err)
    {
     $("#erromessage")[0].innerHTML ="Authorization failed (Error)";
    }
       // alert(document.cookie);
    return false;
    }
var lastClicked = 0;
function forceNav(pageid,recordsPerPage,tableId,pkField)
{

    var pgEntered = $("#txtNavCtrl")[0].value;
    lastClicked =  pgEntered-1 ;
    reloadListWithContent(pageid,lastClicked,recordsPerPage,tableId,pkField);
}
function forceNextPageNav(pageid,recordsPerPage,tableId,pkField)
{
    lastClicked ++ ;
    reloadListWithContent(pageid,lastClicked,recordsPerPage,tableId,pkField);
}

function forcePrevPageNav(pageid,recordsPerPage,tableId,pkField)
{

    if (lastClicked >0 )
            lastClicked -- ;
    reloadListWithContent(pageid,lastClicked,recordsPerPage,tableId,pkField);
}
function reloadListWithContent(pageid,currentPage,recordsPerPage,tableId,pkField)
{
    lastClicked = currentPage;
    console.log(pageid);
    var from = currentPage * recordsPerPage;
    var to= from  + recordsPerPage;
    let hdnContent= document.getElementById("hdnAppliedFilter").value;
    postContent = {} ;
    if( hdnContent != '' )
        postContent = JSON.parse(hdnContent);

    //alert(postContent);
    //postContent = {"code" :"CD102"};
    let request = formRequest("POST",url+'api/commonui/getListContent?pageid='+ pageid + "&from=" + from + "&to=" + to);
    setToken(request);
    request.send(JSON.stringify(postContent),true) ;
    var snapsotresponse  =   JSON.parse(request.responseText)  ;
    console.log("Response from reloadListWithContent =" + request.responseText );
    reRenderTable(tableId,pageid,snapsotresponse.data,pkField);

    var listElems = document.getElementsByName("navListItem") ;
    for (i = 0; i < listElems.length ; i ++ )
    {
            if( i == currentPage)
                listElems[i].className = "page-item  active";
            else
                listElems[i].className = "page-item";

    }
}


function reRenderTable(tableId,entity, data,pkField)
{
        var dataTable = document.getElementById(tableId);
         console.log(dataTable.rows);
        while (dataTable.rows.length > 1) {
                console.log(dataTable.rows.length);
                dataTable.deleteRow(dataTable.rows.length -1 );
        }

        for ( var i in data) {
               singleRow=  data[i];
               //innerContent = "<tr>";
               innerContent = " ";
               innerContent =  innerContent + '<td><input type="checkbox" id="checkbox2" name="options[]" value="' + singleRow[pkField] + '"></td>';
               for ( var i in fields) {
                  var field = fields[i];
                  if (field.ListPageBV != 'IGNORE')
                  {
                    if (field.DisplayControl == 'CheckBox'){
                        if ( singleRow[field.FieldName] == true)
                             innerContent = innerContent + '<td> &#10004 </td>';
                        else
                              innerContent = innerContent + '<td></td>' ;
                    }else {
                        let fieldValue =  singleRow[field.FieldName];
                        console.log(fieldValue);
                        if (typeof fieldValue == 'object' ) {
                            jsonTag = field.JsonTag ;
                            subFields = jsonTag.split('.');
                            secField = subFields[1];
                            console.log('fieldValue = ' + fieldValue) ;
                            console.log('secField =' + secField);
                            innerContent = innerContent + '<td>' + fieldValue[secField] + '</td>';
                        }else
                             innerContent = innerContent + '<td>' + singleRow[field.FieldName] + '</td>';
                      }
                  }
               }
                innerContent = innerContent + '<td>';
                innerContent = innerContent + '<a href="#editDataModel" class="edit" onClick = "populateData(\''+singleRow[pkField]+'\',\'' + entity + '\');" data-toggle="modal"><i class="material-icons" data-toggle="tooltip" title="Edit">&#xE254;</i></a>';
                innerContent = innerContent + '&nbsp';
                innerContent = innerContent + '<a href="#deleteDataModal" class="delete" data-toggle="modal"><i class="material-icons" data-toggle="tooltip" title="Delete">&#xE872;</i></a>';
                innerContent = innerContent + '</td>';
             //   innerContent = innerContent + "</tr>";

                var newrow = dataTable.insertRow();
                newrow.innerHTML =  innerContent;
            }
}

function renderListTable(fields,data, pkField)
{
    console.clear();
    console.log(fields);
    for ( var i in data) {
       singleRow=  data[i];
       console.log(singleRow);
       document.write("<tr>");
       document.write('<td><input type="checkbox" id="checkbox2" name="options[]" value="' + singleRow[pkField] + '"></td>');
       for ( var i in fields) {
          var field = fields[i];
          if (field.ListPageBV != 'IGNORE')
          {

            if (field.DisplayControl == 'CheckBox'){
                if ( singleRow[field.FieldName] == true)
                     document.write('<td> &#10004 </td>');
                else
                      document.write('<td></td>');
            }else {
               let fieldValue =  singleRow[field.FieldName];
               console.log('fieldValue = ' + field.FieldName) ;
                if (typeof fieldValue == 'object'  && fieldValue != null) {
                    jsonTag = field.JsonTag ;
                    subFields = jsonTag.split('.');
                    secField = subFields[1];
                    console.log('fieldValue = ' + fieldValue) ;
                    console.log('secField =' + secField);
                    document.write('<td>' + fieldValue[secField] + '</td>');
                }else {
                    if (fieldValue == null)
                            fieldValue ='';
                    document.write('<td>' + fieldValue + '</td>');
                 }
              }
          }
       }
        document.write('<td>');
        document.write('<a href="#editDataModel" class="edit" onClick = "populateData(\''+singleRow[pkField]+'\',\'' + entity + '\');" data-toggle="modal"><i class="material-icons" data-toggle="tooltip" title="Edit">&#xE254;</i></a>');
        document.write('&nbsp');
        document.write('<a href="#deleteDataModal" class="delete" data-toggle="modal"><i class="material-icons" data-toggle="tooltip" title="Delete">&#xE872;</i></a>');
        document.write('</td>');
        document.write("</tr>");
    }

}


function renderControls(fields, prefixId )
{
    for ( var i in fields) {
        var field = fields[i];
        if (prefixId == "FL")
        {
            if(field.ShowInFilter == true )
           {
                document.write('<div class="col-md-4 col-sm-6 col-6">');
                document.write('<span>' + field.LabelValue + '</span>');
                if (field.DisplayControl == 'CheckBox')
                {
                     document.write(' &nbsp; &nbsp; <input id="chkFL' + field.FieldName + '" type="Checkbox"  data-json="'+ field.JsonTag + '" >');
                }else
                {
                    document.write('<input id="txtFL' + field.FieldName + '" type="'+field.DisplayControl+'"  data-json="'+ field.JsonTag+ '" class="form-control" >');
                }
                document.write('</div>');
           }

        }else if ((prefixId == 'A' && field.AddPageBV != 'IGNORE') || (prefixId == 'Ed' && field.EditPageBV != 'IGNORE')  )
        {
            document.write('<div class="form-group">');
            if ((prefixId == 'A' && field.AddPageBV != 'HIDE') || (prefixId == 'Ed' && field.EditPageBV != 'HIDE') )
            {
                document.write('<label>'+ field.LabelValue + ' </label>');
            }
            if (field.DisplayControl == 'CheckBox')
            {
                 document.write(' &nbsp; &nbsp; <input id="chk' + prefixId + field.FieldName + '" type="Checkbox"  data-json="'+ field.JsonTag + '" >');
            } else if (field.DisplayControl == 'DropDown')
            {
                document.write('<select id="lst' + prefixId + field.FieldName + '"   data-json="'+ field.JsonTag+ '" class="form-control" required>');
                console.log(field.DropDownValues) ;
                    for ( var j in field.DropDownValues)
                    {
                            let entry = field.DropDownValues[j];
                            document.write('<option value="' + entry.Code + '">' + entry.Value + '</option>');
                    }
                document.write('</select>')
            }
            else
            {
                document.write('<input id="txt' + prefixId + field.FieldName + '" type="'+field.DisplayControl+'"  data-json="'+ field.JsonTag+ '" class="form-control" required>');
            }
            document.write('</div>');
        }
    }
}