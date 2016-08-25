
function updatePath(index, currentpath, element) {
	var elementPath = currentpath.slice();
	elementPath.push(index);
	if(!$JQry(element).find("input[name$='canary']").length){
		$JQry(element).find("input[name$='path'],input[name$='filterPath']").val(elementPath);
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
	$JQry("#procedure-sortable ul").sortable({
		connectWith : "#procedure-sortable ul",
		cursor : "move",
		tolerance : "pointer",
		axis: "y",
		forcePlaceholderSize: true,
		placeholder: "procedure-sortable-placeHolder",
		start : function(event, ui) {
			// empêche la sélection du champ
			$JQry(event.originalEvent.target).off();
			$JQry(event.originalEvent.target).on('click',function(e){
				e.preventDefault(); // cas control-label
				e.stopImmediatePropagation();
			});
		},
		stop: function( event, ui ) {
				$JQry("#procedure-sortable > ul").children("li").each(function(index, element){
					updatePath(index, [], element);
				});
				// soumet la mise à jour du formulaire
				$JQry(this).closest("form").find("input[name='updateForm']").click();
		}
	});
	
	$JQry("#procedure-sortable li").click(function(event) {
		// find hidden input by end name
		var path = $JQry(this).children("input[name$='path']").val();
		// add value to form as selected
		selector(this,path,'selectedFieldPath');
		// click hidden selectFilter button
		$JQry(this).closest('form').find("input[name='selectField']").click();
		// empêche de lancer l'évènement click sur le parent
		event.stopPropagation();
		event.preventDefault(); // cas control-label
	});
	
	$JQry("#filter-sortable ul").sortable({
		connectWith : "#filter-sortable ul",
		cursor : "move",
		tolerance : "pointer",
		axis: "y",
		forcePlaceholderSize: true,
		placeholder: "filter-sortable-placeHolder",
		start : function(event, ui) {
			// empêche la sélection du filtre
			$JQry(event.originalEvent.target).off();
			$JQry(event.originalEvent.target).on('click',function(e){
				e.stopImmediatePropagation();
			});
		},
		stop: function(event, ui) {
				$JQry("#filter-sortable > ul").children("li").each(function(index, element){
					updatePath(index, [], element);
				});
				// soumet la mise à jour du formulaire
				$JQry(this).closest("form").find("input[name='updateForm']").click();
		}
	});
	
	$JQry("#filter-sortable li").click(function(event) {
		// find hidden input by end name
		var path = $JQry(this).children("input[name$='filterPath']").val();
		// add value to form as selected
		selector(this,path,'selectedFilterPath');
		// click hidden selectFilter button
		$JQry(this).closest('form').find("input[name='selectFilter']").click();
		// empêche de lancer l'évènement click sur le parent
		event.stopPropagation();
	});
	
	// nested bootstrap tabs
	$JQry("ul.nav-tabs a").click(function (e) {
		e.preventDefault();  
		$JQry(this).tab('show');
	});
});


function selectPath(button, name) {
	var path =$JQry(button).parents("li").find("input[name$='path']").val();
	selector(button, path, name);
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
						filter: params.term
					};
				},
				processResults: function (data, params) {
					return {
						results: data
					};
				},
				cache: true
			},
			escapeMarkup: function (markup) { return markup; },
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
		          filter: params.term
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
		    escapeMarkup: function (markup) { return markup; },
		    minimumInputLength: 3,
		    theme: "bootstrap",
	   	  templateResult: formatProfil,
	   	  templateSelection: formatProfil
		});
	});
};

function initStepSelect(stepSearchUrl){
	$JQry(document).ready(function(){
		$JQry(".stepSelect-select2").select2({
			ajax: {
		      url: stepSearchUrl,
		      dataType: 'json',
		      delay: 300,
		      data: function (params) {
		        return {
		          filter: params.term
		        };
		      },
		      processResults: function (data, params) {
		        return {
		          results: data
		        };
		      },
		      cache: true
		    },
		    escapeMarkup: function (markup) { return markup; },
		    theme: "bootstrap",
	   	  templateResult: formatProfil,
	   	  templateSelection: formatProfil
		});
	});
};
