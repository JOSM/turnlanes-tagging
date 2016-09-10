// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.turnlanestagging.editor.ac;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openstreetmap.josm.gui.tagging.ac.AutoCompletionItemPriority;
import org.openstreetmap.josm.gui.tagging.ac.AutoCompletionListItem;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * This class manages a list of {@link TagSpecification}s.
 *
 * It also provides a method for reading a list of {@link TagSpecification}s from
 * an XML file.
 *
 * @author Gubaer
 *
 */
public class TagSpecifications {

    final static public String ATTR_KEY = "key";
    final static public String ATTR_TYPE = "type";
    final static public String ATTR_FOR_NODE = "for-node";
    final static public String ATTR_FOR_WAY = "for-way";
    final static public String ATTR_FOR_RELATION = "for-relation";
    final static public String ATTR_VALUE = "value";

    final static public String ELEM_ROOT = "osm-tag-definitions";
    final static public String ELEM_TAG = "tag";
    final static public String ELEM_LABEL = "label";

    final static public String DTD = "osm-tag-definitions.dtd";


    /** the default name of the resource file with the  tag specifications */
    static public final String RES_NAME_TAG_SPECIFICATIONS = "/resources/osm-tag-definitions.xml";

    /** the logger object */
    private static Logger logger = Logger.getLogger(TagSpecification.class.getName());

    /** list of tag specifications managed list */
    private ArrayList<TagSpecification> tagSpecifications = null;

    private static TagSpecifications instance = null;

    /**
     * loads the tag specifications from the resource file given by
     * {@link #RES_NAME_TAG_SPECIFICATIONS}.
     *
     * @return the list of {@link TagSpecification}s
     * @throws Exception thrown, if an exception occurs
     */
    static public void loadFromResources() throws Exception  {
        InputStream in = TagSpecifications.class.getResourceAsStream(RES_NAME_TAG_SPECIFICATIONS);
        if (in == null) {
            logger.log(Level.SEVERE, "failed to create input stream for resource '" + RES_NAME_TAG_SPECIFICATIONS + "'");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
	        TagSpecifications spec = new TagSpecifications();
	        spec.load(reader);
	        instance = spec;
        }
    }

    static public TagSpecifications getInstance() throws Exception {
        if (instance == null) {
            loadFromResources();
        }
        return instance;
    }

    /**
     * constructor
     */
    public TagSpecifications() {
        tagSpecifications = new ArrayList<>();
    }


    /**
     * loads the tag specifications from a specific reader
     *
     * @param in  the reader to read from
     * @throws Exception thrown, if an exception occurs
     */
    public void load(Reader in) throws Exception {
        XMLReader parser;

        try {
            parser = XMLReaderFactory.createXMLReader();
            Handler handler = new Handler();
            parser.setContentHandler(handler);
            parser.setErrorHandler(handler);
            parser.setEntityResolver(new ResourceEntityResolver());
            parser.setFeature("http://xml.org/sax/features/validation", true);
            parser.setFeature("http://xml.org/sax/features/namespaces", true);
            parser.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
            parser.parse(new InputSource(in));

        } catch (Exception e) {
            logger.log(Level.SEVERE, "failed to load tag specification file", e);
            e.getCause().printStackTrace();
            throw e;
        } finally {
            parser = null;
        }
    }

    public List<AutoCompletionListItem> getKeysForAutoCompletion(AutoCompletionContext context) {
        ArrayList<AutoCompletionListItem> keys = new ArrayList<>();
        for (TagSpecification spec : tagSpecifications) {
            if (!spec.isApplicable(context)) {
                continue;
            }
            AutoCompletionListItem item = new AutoCompletionListItem();
            item.setValue(spec.getKey());
            item.setPriority(AutoCompletionItemPriority.IS_IN_STANDARD);
            keys.add(item);
        }
        return keys;
    }

    public List<AutoCompletionListItem> getLabelsForAutoCompletion(String forKey, AutoCompletionContext context) {
        ArrayList<AutoCompletionListItem> items = new ArrayList<>();
        for (TagSpecification spec : tagSpecifications) {
            if (spec.getKey().equals(forKey)) {
                List<LabelSpecification> lables = spec.getLables();
                for (LabelSpecification l : lables) {
                    if (!l.isApplicable(context)) {
                        continue;
                    }
                    AutoCompletionListItem item = new AutoCompletionListItem();
                    item.setValue(l.getValue());
                    item.setPriority(AutoCompletionItemPriority.IS_IN_STANDARD);
                    items.add(item);
                }
            }
        }
        return items;
    }

    /**
     * replies a list of {@see KeyValuePair}s for all {@see TagSpecification}s and
     * {@see LableSpecification}s.
     *
     * @return the list
     */
    public ArrayList<KeyValuePair> asList() {
        ArrayList<KeyValuePair> entries = new ArrayList<>();

        for (TagSpecification s : tagSpecifications) {
            for (LabelSpecification l : s.getLables()) {
                entries.add(new KeyValuePair(s.getKey(), l.getValue()));
            }
        }
        return entries;
    }



    /**
     * The SAX handler for reading XML files with tag specifications
     *
     * @author gubaer
     *
     */
    class Handler extends DefaultHandler {

        /**  the current tag specification. Not null, while parsing the content
         * between &lt;tag&gt; ... &lt;/tag&gt;
         */
        private TagSpecification currentTagSpecification  = null;

        @Override
        public void endDocument() throws SAXException {
            logger.log(Level.FINE,"END");
        }

        @Override
        public void error(SAXParseException e) throws SAXException {
            logger.log(Level.SEVERE, "XML parsing error", e);
        }

        @Override
        public void fatalError(SAXParseException e) throws SAXException {
            logger.log(Level.SEVERE, "XML parsing error", e);
        }

        @Override
        public void startDocument() throws SAXException {
            logger.log(Level.FINE,"START");
        }

        /**
         * parses a string value consisting of 'yes' or 'no' (exactly, case
         * sensitive)
         *
         * @param value the string value
         * @return true, if value is <code>yes</code>; false, if value is <code>no</code>
         * @throws SAXException thrown, if value is neither <code>yes</code> nor <code>no</code>
         */
        protected boolean parseYesNo(String value) throws SAXException {
            if ("yes".equals(value))
                return true;
            else if ("no".equals(value))
                return false;
            else
                throw new SAXException("expected 'yes' or 'no' as attribute value, got '" + value + "'");
        }

        /**
         * handles a start element with name <code>osm-tag-definitions</code>
         *
         * @param atts  the XML attributes
         * @throws SAXException
         */
        protected void startElementOsmTagDefinitions(Attributes atts) throws SAXException {
            tagSpecifications = new ArrayList<>();
        }

        /**
         * handles an end element with name <code>osm-tag-specifications</code>
         *
         * @throws SAXException
         */
        protected void endElementOsmTagDefinitions() throws SAXException {
            // do nothing
        }

        /**
         * handles a start element with name <code>tag</code>
         *
         * @param atts the XML attributes of the element
         * @throws SAXException
         */
        protected void startElementTag(Attributes atts) throws SAXException {
            currentTagSpecification = new TagSpecification();
            for (int i=0; i< atts.getLength(); i++) {
                String name = atts.getQName(i);
                String value = atts.getValue(i);

                if (ATTR_KEY.equals(name)) {
                    currentTagSpecification.setKey(value);
                } else if (ATTR_TYPE.equals(name)) {
                    currentTagSpecification.setType(value);
                } else if (ATTR_FOR_NODE.equals(name)) {
                    currentTagSpecification.setApplicableToNode(parseYesNo(value));
                } else if (ATTR_FOR_WAY.equals(name)) {
                    currentTagSpecification.setApplicableToWay(parseYesNo(value));
                } else if (ATTR_FOR_RELATION.equals(name)) {
                    currentTagSpecification.setApplicableToRelation(parseYesNo(value));
                } else
                    throw new SAXException("unknown attribut '" + name + "' on element 'tag'");
            }
        }


        /**
         * handles an end element with name <code>tag</code>
         * @throws SAXException
         */
        protected void endElementTag() throws SAXException {
            tagSpecifications.add(currentTagSpecification);
            currentTagSpecification = null;

        }

        /**
         * handles a start element with name <code>label</code>
         *
         * @param atts the XML attributes
         * @throws SAXException
         */
        protected void startElementLabel(Attributes atts) throws SAXException {
            LabelSpecification ls = new LabelSpecification();
            for (int i=0; i< atts.getLength(); i++) {
                String name = atts.getQName(i);
                String value = atts.getValue(i);

                if (ATTR_VALUE.equals(name)) {
                    ls.setValue(value);
                } else if (ATTR_FOR_NODE.equals(name)) {
                    ls.setApplicableToNode(parseYesNo(value));
                } else if (ATTR_FOR_WAY.equals(name)) {
                    ls.setApplicableToWay(parseYesNo(value));
                } else if (ATTR_FOR_RELATION.equals(name)) {
                    ls.setApplicableToRelation(parseYesNo(value));
                } else
                    throw new SAXException("unknown attribut '" + name + "' on element 'lable'");
            }
            currentTagSpecification.addLable(ls);
        }

        /**
         * handles an end element with name <code>label</code>
         *
         * @throws SAXException
         */
        protected void endElementLabel() throws SAXException {
            // do nothing
        }

        @Override
        public void startElement(String namespaceURI, String localName, String qName,
                Attributes atts) throws SAXException {
            if (ELEM_ROOT.equals(qName)) {
                startElementOsmTagDefinitions(atts);
            } else if (ELEM_TAG.equals(qName)) {
                startElementTag(atts);
            } else if (ELEM_LABEL.equals(qName)) {
                startElementLabel(atts);
            } else
                throw new SAXException("unknown element '" + qName + "'");
        }

        @Override
        public void endElement(String namespaceURI, String localName, String qName)
        throws SAXException {
            if (ELEM_ROOT.equals(qName)) {
                endElementOsmTagDefinitions();
            } else if (ELEM_TAG.equals(qName)) {
                endElementTag();
            } else if (ELEM_LABEL.equals(qName)) {
                endElementLabel();
            } else
                throw new SAXException("unknown element '" + qName + "'");
        }

        @Override
        public void warning(SAXParseException e) throws SAXException {
            logger.log(Level.WARNING, "XML parsing warning", e);
        }
    }

    static class ResourceEntityResolver implements EntityResolver {

        @Override
	public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            if (systemId != null && systemId.endsWith(DTD)) {
                InputStream stream = TagSpecifications.class.getResourceAsStream("/resources/"+DTD);
                if (stream == null) {
                    logger.log(Level.WARNING, "Unable to find DTD: "+DTD);
                }
                return stream != null ? new InputSource(stream) : null;
            } else {
                throw new SAXException("couldn't load external DTD '" + systemId + "'");
            }
        }
    }

    static public void main(String args[]) throws Exception{
        TagSpecifications.loadFromResources();
    }
}
