
	$(document).ready(function () {
		$("#buttonCancel").on("click", function () {
			window.location = moduleURL
		})
		
		$("#fileImage").change(function(){
	        var fileSize=this.files[0].size;
	        if(fileSize>102400)
	        {
	         this.setCustomValidity("you must choose an image less than 100KB")
	         this.reportValidity();
	        }
	        else
	        {
	        this.setCustomValidity("")
	        showImageThumbnail(this)
	        }
		})
	})
	
	function  showImageThumbnail(fileInput)
	{
	   console.log("FileInput->this",fileInput);
	   var file=fileInput.files[0];
	   console.log("fileVariable",file);
	   var reader=new FileReader();
	   reader.onload= function(e){
	      console.log("e",e);
	      console.log("Result",e.target.result);
	      $("#thumbnail").attr("src",e.target.result);
	   }
	   reader.readAsDataURL(file);
	}

 function showDialogModule(title, message) {
		$("#modalTitle").text(title);
		$("#modalBody").text(message);
		$('#modalDialog').modal();
	}
	
	function showErrorModel(message)
	{
	  showDialogModule("Error",message);
	}
		function showWarningModel(message)
	{
	  showDialogModule("Warning",message);
	}