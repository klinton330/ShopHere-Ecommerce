function clearFilter(){
	window.location=moduleURL
}

function showDeleteConfirmModel(link,entityName){
	entityId = link.attr("entityId")
				$("#confirmmodalDialog").modal();
				$("#confirmText").text("Are you want to delete this "+entityName+" ID " + entityId + "?")
				$("#yesButton").attr("href", link.attr("href"))
			}
