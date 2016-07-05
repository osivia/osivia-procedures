
function updatePath(index, currentpath, element) {
	var elementPath = currentpath.slice();
	elementPath.push(index);
	if(!$JQry(element).find("input[name$='canary']").length){
		$JQry(element).find("input[name$='path']").val(elementPath);
		$JQry('<input>').attr({
			type : 'hidden',
			name : 'canary',
			value : 'true'
		}).appendTo(element);
	}
	
	var nestedPath = elementPath.slice();
	if($JQry(element).find("ul").length){
		$JQry(element).find("ul").children("li").each(function(loopIndex,element){
			updatePath(loopIndex, nestedPath, element);
		});
	}
}

$JQry(function() {
	// Sortable
	$JQry("#procedure-sortable ul").sortable({
		connectWith : "#procedure-sortable ul",
		cursor : "move",
		tolerance : "pointer",
		axis: "y",
		forcePlaceholderSize: true,
		placeholder: "bg-info",
		stop: function( event, ui ) {
				$JQry("#procedure-sortable > ul").children("li").each(function(index, element){
					updatePath(index, [], element);
				});	
				$JQry(this).closest("form").find("input[name='updateForm']").click();
		}
	});
});

function selectPath(button, name) {
	var path =$JQry(button).parents("li").find("input[name$='path']").val();
	selector(button, path, name)
}

function selector(button, value, name) {
	var form = $JQry(button).closest('form');
	$JQry('<input>').attr({
		type : 'hidden',
		name : name,
		value : value
	}).appendTo(form);
};

function formatProfil (group) {
	return group.text + ' (' + group.id + ')';
};

function select2Vocab(vocabularySearchUrl,selectId){
	
	$JQry(document).ready(function(){
		$JQry("#"+selectId).select2({
			ajax: {
				url: vocabularySearchUrl,
				dataType: 'json',
				delay: 300,
				data: function (params) {
					return {
						filter: params.term // search term
					};
				},
				processResults: function (data, params) {
					return {
						results: data
					};
				},
				cache: true
			},
			escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
			theme: "bootstrap",
		});
	});
};

function initGroupSelect(groupSearchUrl){
	$JQry(document).ready(function(){
		$JQry(".groupSelect-select2").select2({
		    ajax: {
		      url: groupSearchUrl,
		      dataType: 'json',
		      delay: 300,
		      data: function (params) {
		        return {
		          filter: params.term // search term
		        };
		      },
		      processResults: function (data, params) {
		        return {
		          results: $JQry.map(data, function(group) {
		        	  return { id: group.cn, text: group.displayName };
		          })
		        };
		      },
		      cache: true
		    },
		    escapeMarkup: function (markup) { return markup; }, // let our custom formatter work
		    minimumInputLength: 3,
		    theme: "bootstrap",
	   	  templateResult: formatProfil,
	   	  templateSelection: formatProfil
		});
	});
}

function hideModal(element){
	$JQry(element).parents(".modal").modal('hide');
}

//$JQry(document).ready(function(){
//	$JQry(".procedure-sortable li").hover(
//			function() {
//				// on retire la class hover aux elements qui l'ont
//				$JQry(".procedure-hover").removeClass("procedure-hover");
//				// on l'ajoute à l'élément courant
//				$JQry(this).addClass("procedure-hover");
//			}, function() {
//				// on retire la class hover à l'élément courant
//				$JQry(this).removeClass("procedure-hover");
//				// on simule un nouvel hover sur le parent direct si applicable
//				$JQry(this).parent().closest(".procedure-sortable li").mouseenter();
//			}
//	);
//});
