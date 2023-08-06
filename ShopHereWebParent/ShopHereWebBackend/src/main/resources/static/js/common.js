	
	      $(document).ready(function() {
	        $("#logoutlink").on("click" ,function(e)
	      {
	      e.preventDefault();
	      document.logout.submit();
	      });
	      customizeDropdownMenu();
	      });
	      
	      
	      function customizeDropdownMenu(){
			  $(".navbar .dropdown").hover(function(){
				  $(this).find('.dropdown-menu').first().stop(true,true).delay(250).slideDown();
			  },
			  function(){
				  $(this).find('.dropdown-menu').first().stop(true,true).delay(250).slideUp();
			  })
			  $(".dropdown>a").click(function(){
				  //location.href=http://localhost:8080/ShopHereAdmin/?continue
				  //this.href=http://localhost:8080/ShopHereAdmin/account
				  location.href=this.href;
			  })
		  }
