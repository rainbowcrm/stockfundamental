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
 getStockList(displayPage-1,recordsPerPage);
}

function nextPage(recordsPerPage)
{
 getStockList(displayPage+1,recordsPerPage);
}

 function getStockList(currentPage,recordsPerPage)
 {
 console.log(url);
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
     var from = currentPage * recordsPerPage;
     var to= from  + recordsPerPage;
     let request = formRequest("GET",url+'uiapi/getAllStocks?from=' + from + '&to=' + to);
     setToken(request);
     request.send() ;
     var snapsotresponse  =   JSON.parse(request.responseText)  ;
     console.log("Responss   e =" + request.responseText );
     reRenderTable('dataTableExample1',snapsotresponse);


 }

function getAllStockCount()
{
    let request = formRequest("GET",url+'uiapi/getAllStockCount');
     setToken(request);
     request.send() ;
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
                innerContent =  innerContent + '<td>' + singleRow['currentPrice'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['eps'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['bookvalue'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['revenue'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['profit'] + '</td>';
                innerContent =  innerContent + '<td>' + singleRow['divident'] + '</td>';
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