
function updateOrdersAndPath(order, currentpath, element) {
	var elementPath = currentpath.slice();
	elementPath.push(order);
	$JQry(element).find("input[name$='path']").val(elementPath);
	
	var nestedPath = elementPath.slice();
	if($JQry(element).find("ul").length){
		$JQry(element).find("ul").children("li").each(function(index,element){
			updateOrdersAndPath(index, nestedPath, element);
		});
	}
}

$JQry(function() {
	// Sortable
	$JQry("#procedure-sortable ul").sortable({
		connectWith : "#procedure-sortable ul",
		cursor : "move",
		handle : ".sortable-handle",
		helper : "clone",
		tolerance : "pointer",
		axis: "y",
		forcePlaceholderSize: true,
		placeholder: "bg-info",
		update: function( event, ui ) {
			$JQry("#procedure-sortable > ul").children("li").each(function(index, element){
				updateOrdersAndPath(index, [], element);
			});
			$JQry(this).closest("form").find("input[name='updateForm']").click();
		}
	});
});

function selectPath(button, name) {
	var path =$JQry(button).parents("li").find("input[name='path']").val();
	selector(button, path, name)
}

function selectOrder(button, name) {
	var order =$JQry(button).parents("li").find("input[name='order']").val();
	selector(button, order, name)
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


