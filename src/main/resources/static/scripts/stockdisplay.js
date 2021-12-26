 url = "https://localhost:20452/";
 displayPage = 0;

 function formRequest(methodType,api)
 {
       let request = new XMLHttpRequest () ;
       request.open(methodType,api,false);
       request.setRequestHeader("Content-type", "application/json");
       request.setRequestHeader("Access-Control-Allow-Origin", url);
       console.log(api);
       return request ;
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

function previousPage(recordsPerPage)
{
 applyFilter(displayPage-1,recordsPerPage);
}

function nextPage(recordsPerPage)
{
 applyFilter(displayPage+1,recordsPerPage);
}
function enableDisableNextPrev(currentPage,totalPages)
{
if (currentPage > 0) {
    $("#btnPrev").attr('class',"page-item ") ;
 }else
 {
   $("#btnPrev").attr('class',"page-item disabled") ;
 }
 if (currentPage == totalPages-1) {
   $("#btnNext").attr('class',"page-item disabled") ;
  }else
  {
   $("#btnNext").attr('class',"page-item") ;
  }
  displayPage= currentPage ;
  $("#navDetails").html("page " + (displayPage+1) + " of " + totalPages  );
}
 function getStockList(currentPage,recordsPerPage)
 {
 console.log(url);
     enableDisableNextPrev(currentPage,totalPages) ;
     var from = currentPage * recordsPerPage;
     var to= from  + recordsPerPage;
     let request = formRequest("GET",url+'uiapi/getAllStocks?from=' + from + '&to=' + to);
     setToken(request);
     request.send() ;
     var snapsotresponse  =   JSON.parse(request.responseText)  ;
     console.log("Responss   e =" + request.responseText );
     reRenderTable('dataTableExample1',snapsotresponse);


 }
function getAllIndustries()
{
    let request = formRequest("GET",url+'uiapi/getDistinctIndustry');
     setToken(request);
     request.send() ;
     var snapsotresponse  =   JSON.parse(request.responseText)  ;
     console.log("All Industries =" + request.responseText );
     return snapsotresponse;
}

function getAllSectors()
{
    let request = formRequest("GET",url+'uiapi/getDistinctSector');
     setToken(request);
     request.send() ;
     var snapsotresponse  =   JSON.parse(request.responseText)  ;
     console.log("All Sectors =" + request.responseText );
     return snapsotresponse;
}
function writePaginationButtons()
{

 var innerHTML = '<li  class="page-item">';

    innerHTML +=  '<span class="page-link" style="color:black" id="navDetails" ></span>';
    innerHTML +=  '</li>';
    innerHTML +=  '<li id="btnPrev"  class="page-item disabled">';
    innerHTML +=  '<a class="page-link" onclick = "previousPage(30)"  href="#" tabindex="-1">Previous</a>';
    innerHTML +=   '</li>';
allStockCount = getAllStockCount();
totalPages= Math.ceil(allStockCount/30);
console.log('allStockCount=' +  allStockCount + ":: totalPages=" + totalPages);
recordsPerPage = 30;
currentPage = 1;
if ( totalPages <=10 ) {
        for ( i = 1 ; i <= totalPages ; i ++ ) {
        innerHTML += '<li class="page-item"><a class="page-link" onclick = "applyFilter('+ (i-1) +','+ recordsPerPage +')" href="#">'+ (i) +'</a></li>';
     }
}else {
     skipIndex = Math.floor(totalPages/10) + 1;
     console.log('skipIndex ='  + skipIndex );
    for ( i = 1 ; i <= totalPages ; i+=skipIndex ) {
        innerHTML += '<li class="page-item"><a class="page-link" onclick = "applyFilter('+ (i-1) +','+ recordsPerPage +')"  href="#">'+ i +'</a></li>';
     }
     console.log('i=' + i );
     if ( i < totalPages+skipIndex) {
     innerHTML += '<li class="page-item"><a class="page-link" onclick = "applyFilter('+ (totalPages-1) +','+ recordsPerPage +')"  href="#">'+ totalPages +'</a></li>';
     }
}
   innerHTML += '<li id="btnNext"  class="page-item">';
   innerHTML +=  '<a class="page-link"  onclick = "nextPage(30)"  href="#">Next</a>';
   innerHTML +=   '</li>';

   $('#idPaginationCtrls').html(innerHTML);
}

function getFilterJSON()
{
    let postContent= { };
    industry = $("#selIndustry").val() ;
    sector =   $("#selSector").val();
    security =   $("#txtSecurty").val() ;
   bseCode =   $("#txtBSECode").val() ;
   capGroup = $("#selMCGroup").val() ;
   console.log(industry);
   console.log(sector);
   console.log(security);
   console.log(bseCode);

   if ( industry != '0'){
   postContent['industry'] = industry
   }
   if ( sector != '0'){
      postContent['sector'] = sector;
      }
   if ( security.trim() != ''){
      postContent['company'] = security;
      }
      if ( bseCode.trim() != ''){
         postContent['bseCode'] = bseCode;
         }
     if ( capGroup != '0'){
     postContent['marketGroup'] = capGroup;
     }
   return postContent ;
}

function clearFilter(recordsPerPage)
{
$("#selIndustry").val("0");
$("#selSector").val("0");
$("#txtSecurty").val("");
 $("#txtBSECode").val("") ;
 applyFilter(0,recordsPerPage);

}
function downloadReport(api,typeResp)
{

 let request = new XMLHttpRequest () ;
        request.open("GET",api);
        request.setRequestHeader("Content-type", "application/json");
        request.setRequestHeader("Access-Control-Allow-Origin", url);
      setToken(request);
      $("#btnRepIcon").removeClass('fa fa-file');
      $("#btnRepIcon").addClass('fa fa-spinner fa-spin');
      $("#btnRepIcon").prop('disabled', true);
      request.responseType = "arraybuffer";
      //"fa fa-spinner fa-spin"
      request.onload = function ()  {
           if (this.status === 200) {
               var blob = new Blob([request.response], {type: typeResp});
               var objectUrl = URL.createObjectURL(blob);
               $("#btnRepIcon").removeClass('fa fa-spinner fa-spin');
               $("#btnRepIcon").addClass('fa fa-file');
               $("#btnRepIcon").prop('disabled', false);
               if ('application/csv' == typeResp)
               {
                  var a = document.createElement('a');
                         a.href = objectUrl;
                         a.download = 'repSum.csv';
                         a.click();
               }else {
               window.open(objectUrl);
               }
           }
       };
       request.send() ;
}
function submitReport()
{
  let fromDate = $("#dtFrom").val();
  let toDate = $("#dtTo").val();
  let format = $("#selFormat").val();
  let grp = $("#selIndustry").val();

  console.log('fromDate='+ fromDate);
  console.log('toDate='+ toDate);

 if ("PDF" == format)
  downloadReport (url+'reports/getTransSummary?fromDate='+fromDate+"&toDate="+ toDate+"&groupBy="+ grp + "&repFormat="+ format,'application/pdf');
  else
  downloadReport (url+'reports/getTransSummary?fromDate='+fromDate+"&toDate="+ toDate+"&groupBy="+ grp + "&repFormat="+ format,'application/csv');
       /*var myresponse = new Blob([request.response], {type : 'application/pdf'});
       var a = document.createElement('a');
       a.href = window.URL.createObjectURL(myresponse);
       a.download = 'repSum.pdf';
       a.click();*/


}
function applyFilter(currentPage,recordsPerPage)
{
    postContent = getFilterJSON();
  writePaginationButtons();
  enableDisableNextPrev(currentPage,totalPages) ;
       var from = currentPage * recordsPerPage;
       var to= from  + recordsPerPage;
 console.log( postContent) ;
 let request = formRequest("POST",url+'uiapi/applyFilterStocks?from=' + from + '&to=' + to);
     setToken(request);
     request.send(JSON.stringify(postContent),true) ;
     var snapsotresponse  =   JSON.parse(request.responseText)  ;
         console.log("Response from reloadListWithContent =" + request.responseText );
         reRenderTable('dataTableExample1',snapsotresponse);

}
function tableExport(fileType)
{
    postContent = getFilterJSON();
     let request = new XMLHttpRequest () ;
            request.open("POST",url+'uiapi/downloadFile/'+fileType);
            request.setRequestHeader("Content-type", "application/json");
            request.setRequestHeader("Access-Control-Allow-Origin", url);
         setToken(request);
         request.responseType = "arraybuffer";
               request.onload = function ()  {
                    if (this.status === 200) {
                        var blob = new Blob([request.response], {type: "application/vnd.ms-excel"});
                        var objectUrl = URL.createObjectURL(blob);
                        window.open(objectUrl);
                    }
                };

         request.send(JSON.stringify(postContent)) ;

         /*var myresponse = new Blob([data], {type : 'application/octet-stream'});
         var a = document.createElement('a');
         a.href = window.URL.createObjectURL(myresponse);
         a.download = 'report_new.xls';
         a.click(); */
}
function getAllStockCount()
{
     postContent = getFilterJSON();
    let request = formRequest("POST",url+'uiapi/filteredStockCount');
     setToken(request);
    request.send(JSON.stringify(postContent),true) ;
     var snapsotresponse  =   JSON.parse(request.responseText)  ;
     console.log("Responsse =" + request.responseText );
     return request.responseText;

}
 function reRenderTable(tableId,data)
 {
         var dataTable = document.getElementById(tableId);
         while (dataTable.rows.length > 1) {
                 dataTable.deleteRow(dataTable.rows.length -1 );
         }

         for ( var i in data) {
                singleRow=  data[i];
                //innerContent = "<tr>";
                var bgColor = i%2==0?'beige':'white';
                innerContent = '<tr data-row="' + bgColor +  '">';
                innerContent =  innerContent + '<td>' + singleRow['bseCode'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['stock'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['industry'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['sector'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['group'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['marketCap'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['groupCap'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['currentPrice'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['eps'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['bookvalue'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['revenue'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['profit'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['divident'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['pe'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['pb'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['roe'] + '</td>';
                innerContent =  innerContent + '</tr>';
                 var newrow = dataTable.insertRow();
                 newrow.innerHTML =  innerContent;
             }
            applyColor();

 }

 function applyColor()
 {
    $("tr:odd").css("background-color", "white");
    $("tr:even").css("background-color", "aliceblue");
 }

 function getDashBoard()
 {
  let request = formRequest("GET",url+'uiapi/getDashBoardData?days=30');
      setToken(request);
      request.send() ;
      var snapsotresponse  =   JSON.parse(request.responseText)  ;
      console.log("Dashboard Response =" + request.responseText );
      return snapsotresponse;

 }