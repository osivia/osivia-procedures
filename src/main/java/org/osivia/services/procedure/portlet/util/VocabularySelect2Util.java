package org.osivia.services.procedure.portlet.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;


/**
 * Util class for nuxeo Vocabulary used in select2
 * 
 * @author Dorian Licois
 */
public class VocabularySelect2Util {

    /**
     * Parse JSON array with filter.
     *
     * @param array JSON array
     * @param filter filter, may be null
     * @return results
     * @throws IOException
     */
    public static JSONArray parse(JSONArray array, String filter) throws IOException {
        final Map<String, Item> items = new HashMap<String, Item>(array.size());
        final Set<String> rootItems = new LinkedHashSet<String>();

        boolean multilevel = false;

        final Iterator<?> iterator = array.iterator();
        while (iterator.hasNext()) {
            final JSONObject object = (JSONObject) iterator.next();
            final String key = object.getString("key");
            final String value = object.getString("value");
            String parent = null;
            if (object.containsKey("parent")) {
                parent = object.getString("parent");
            }
            final boolean matches = StringUtils.isBlank(filter) ? true : matches(value, filter);

            Item item = items.get(key);
            if (item == null) {
                item = new Item(key);
                items.put(key, item);
            }
            item.value = value;
            item.parent = parent;
            if (matches) {
                item.matches = true;
                item.displayed = true;
            }

            if (StringUtils.isEmpty(parent)) {
                rootItems.add(key);
            } else {
                multilevel = true;

                Item parentItem = items.get(parent);
                if (parentItem == null) {
                    parentItem = new Item(parent);
                    items.put(parent, parentItem);
                }
                parentItem.children.add(key);

                if (item.displayed) {
                    while (parentItem != null) {
                        parentItem.displayed = true;

                        if (StringUtils.isEmpty(parentItem.parent)) {
                            parentItem = null;
                        } else {
                            parentItem = items.get(parentItem.parent);
                        }
                    }
                }
            }
        }


        final JSONArray results = new JSONArray();
        generateChildren(items, results, rootItems, multilevel, 1, null);

        return results;
    }


    /**
     * Check if value matches filter.
     *
     * @param value value
     * @param filter filter
     * @return true if value matches filter
     * @throws UnsupportedEncodingException
     */
    private static boolean matches(String value, String filter) throws UnsupportedEncodingException {
        boolean matches = true;

        if (filter != null) {
            // Decoded value
            final String decodedValue = URLDecoder.decode(value, "UTF-8");
            // Diacritical value
            final String diacriticalValue = Normalizer.normalize(decodedValue, Normalizer.Form.NFD).replaceAll("\\p{IsM}+", StringUtils.EMPTY);

            // Filter
            final String[] splittedFilters = StringUtils.split(filter, "*");
            for (final String splittedFilter : splittedFilters) {
                // Diacritical filter
                final String diacriticalFilter = Normalizer.normalize(splittedFilter, Normalizer.Form.NFD).replaceAll("\\p{IsM}+", StringUtils.EMPTY);

                if (!StringUtils.containsIgnoreCase(diacriticalValue, diacriticalFilter)) {
                    matches = false;
                    break;
                }
            }
        }

        return matches;
    }


    /**
     * Generate children.
     *
     * @param items vocabulary items
     * @param array results JSON array
     * @param children children
     * @param optgroup options group presentation indicator
     * @param level depth level
     * @param parentId parent identifier
     * @throws UnsupportedEncodingException
     */
    private static void generateChildren(Map<String, Item> items, JSONArray array, Set<String> children, boolean optgroup, int level, String parentId)
            throws UnsupportedEncodingException {
        for (final String child : children) {
            final Item item = items.get(child);
            if ((item != null) && item.displayed) {
                // Identifier
                String id;
                if (parentId == null) {
                    id = item.key;
                } else {
                    id = parentId + "/" + item.key;
                }

                final JSONObject object = new JSONObject();
                object.put("id", id);
                object.put("text", URLDecoder.decode(item.value, "UTF-8"));
                object.put("optgroup", optgroup);
                object.put("level", level);

                if (!item.matches) {
                    object.put("disabled", true);
                }

                array.add(object);

                if (!item.children.isEmpty()) {
                    generateChildren(items, array, item.children, false, level + 1, id);
                }
            }
        }
    }

    /**
     * Vocabulary item java-bean.
     *
     * @author Cédric Krommenhoek
     */
    private static class Item {

        /** Vocabulary key. */
        private final String key;
        /** Vocabulary children. */
        private final Set<String> children;

        /** Vocabulary value. */
        private String value;
        /** Vocabulary parent. */
        private String parent;
        /** Displayed item indicator. */
        private boolean displayed;
        /** Filter matches indicator. */
        private boolean matches;


        /**
         * Constructor.
         *
         * @param key vocabulary key
         */
        public Item(String key) {
            super();
            this.key = key;
            children = new HashSet<String>();
        }

    }

}
