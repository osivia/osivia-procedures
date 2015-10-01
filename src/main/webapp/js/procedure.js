
$JQry(function() {
	// Sortable
	$JQry(".procedure-sortable").sortable({
		connectWith : ".procedure-sortable",
		cursor : "move",
		handle : ".sortable-handle",
		helper : "clone",
		tolerance : "pointer",

		update : function(event, ui) {
			var $form = $JQry(this).closest("form");
			var count = 0;
			$form.find("input[name$=order]").each(function(index, element) {
				$JQry(element).val(count);
				count++;
			});
		}
	});
});

function selector(button, index, name) {
	var form = $JQry(button).closest('form');
	$JQry('<input>').attr({
		type : 'hidden',
		name : name,
		value : index
	}).appendTo(form);
}
