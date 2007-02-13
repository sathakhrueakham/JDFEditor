package org.cip4.jdfeditor;
/*
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2006 The International Cooperation for the Integration of
 * Processes in  Prepress, Press and Postpress (CIP4).  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        The International Cooperation for the Integration of
 *        Processes in  Prepress, Press and Postpress (www.cip4.org)"
 *    Alternately, this acknowledgment mrSubRefay appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "CIP4" and "The International Cooperation for the Integration of
 *    Processes in  Prepress, Press and Postpress" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact info@cip4.org.
 *
 * 5. Products derived from this software may not be called "CIP4",
 *    nor may "CIP4" appear in their name, without prior writtenrestartProcesses()
 *    permission of the CIP4 organization
 *
 * Usage of this software in commercial products is subject to restrictions. For
 * details please consult info@cip4.org.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE INTERNATIONAL COOPERATION FOR
 * THE INTEGRATION OF PROCESSES IN PREPRESS, PRESS AND POSTPRESS OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIrSubRefAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the The International Cooperation for the Integration
 * of Processes in Prepress, Press and Postpress and was
 * originally based on software restartProcesses()
 * copyright (c) 1999-2001, Heidelberger Druckmaschinen AG
 * copyright (c) 1999-2001, Agfa-Gevaert N.V.
 *
 * For more information on The International Cooperation for the
 * Integration of Processes in  Prepress, Press and Postpress , please see
 * <http://www.cip4.org/>.
 *
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import org.cip4.jdflib.core.AttributeName;
import org.cip4.jdflib.core.JDFDoc;
import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFParser;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.core.VString;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourcePool;
import org.cip4.jdflib.resource.JDFResource;
import org.cip4.jdflib.util.JDFDate;
import org.cip4.jdflib.util.MimeUtil;
import org.cip4.jdflib.util.UrlUtil;

/**
 * static utilities for the editor
 * @author prosirai
 *
 */
public class EditorUtils
{
    /**
     * Adds the indexes to the Vector vInvalidAt.
     * @param vInvalidAt - The Vector with the invalid indexes
     * @param names      - The Vector with the invalid names
     * @param nStr       - The attribute name
     * @param index      - The position in the loop
     */
    public static void setInvalidVec(Vector vInvalidAt, Vector names, String nStr, int index)
    {
        for (int i = 0; i < names.size(); i++)
        {
            if(names.elementAt(i).equals(nStr))
                vInvalidAt.add(Integer.toString(index));
        }
    }
    /**
     * Check if a KElement is valid or not.
     * @param elem - The KElement you want to check.
     * @return True if the KElement is valid, false otherwise.
     */
    public static boolean elemIsValid(KElement elem)
    {
        if(!(elem instanceof JDFElement) || (!JDFElement.isInJDFNameSpaceStatic(elem)))
        {
            return true;
        }
        return elem.isValid(KElement.EnumValidationLevel.Complete);
    }
    
    /**
     * Method setValueForNewAttribute.
     * set a value to the new attribute if nothing is declared the value
     * will be "New Value"
     * @param att
     */
    static public String  setValueForNewAttribute(KElement e,String attName)
    {
        //TODO move this to JDFElement and let JDFLib predefine reasonable attributes 
        if (attName.equals("ID"))
            return JDFElement.uniqueID(0);

        if (attName.equals("JobID"))
            return JDFElement.uniqueID(0);

        if (attName.equals("JobPartID") &&(e instanceof JDFElement))
        {
            return ((JDFElement)e).generateDotID("JobPartID", null);
        }

        if (attName.equals("Status") &&(e instanceof JDFNode))
        {
            return "Waiting";
        }
        if (attName.equals("Status") &&(e instanceof JDFResource))
        {
            return "Unavailable";
        }

        if (attName.equals("Type"))
            return "Product";
        
        if (attName.equals("TimeStamp"))
            return new JDFDate().getDateTimeISO();
            
        if (attName.equals("ComponentType"))
            return "PartialProduct";
        
        if (attName.equals("PreviewType"))
            return "Separation";
        
        return "New Value";
    }
    
    /**
     * gets all resources from this node and from all its ancestors.
     * @return - vector of all resources allowed to be linked from the level of jdfNode
     */
    public static VElement getResourcesAllowedToLink(JDFNode jdfNode, EnumUsage usage)
    {
        VElement resourcesInTree = new VElement();
        JDFNode nodeTmp=jdfNode;
        while (nodeTmp != null)   
        {
            JDFResourcePool resPool = nodeTmp.getResourcePool();
            if (resPool != null)        
            {
                VElement vRes = resPool.getPoolChildren(null, null, null);
                resourcesInTree.addAll(vRes);
            } 
            nodeTmp = nodeTmp.getParentJDF(); 
        } 
        VElement vResLinks=jdfNode.getResourceLinks(null, null, null);
        
        final VString vValidLinks = jdfNode.linkNames();
        if(vValidLinks==null)
            return null;
        vValidLinks.unify();
        final VString vValidInfo = jdfNode.linkInfo();
        int iAny=vValidLinks.index("*");
        
        for (int i = resourcesInTree.size()-1; i >= 0; i--)
        {
            final JDFResource res = (JDFResource) resourcesInTree.item(i);
            String resName = res.getNodeName();
            int vRLSize=vResLinks==null ? 0 : vResLinks.size();
                
            String id=res.getID();
            if(id!=null)
            {
                for(int j=0;j<vRLSize;j++)
                {
                    if(id.equals(vResLinks.item(j).getAttribute(AttributeName.RREF)))
                    {
                        resourcesInTree.remove(res);
                        continue;
                    }
                }
            }
            int n=vValidLinks.index(resName);
            if(n<0)
                n=iAny;
            
            if (n<0)
            {
                resourcesInTree.remove(res);
            }
            else if(usage!=null)
            {
                String io= usage.equals(EnumUsage.Input) ? "i" : "o";
                if(vValidInfo.stringAt(n).indexOf(io)<0)
                {
                    resourcesInTree.remove(res);
                }
            }
             
        }
        return resourcesInTree.size()==0 ? null : resourcesInTree;
    }
    
    
////////////////////////////////////////////////////////////////////////////   
    
    /**
     * Method getAttributeOptions
     * @param w
     * @return
     */
    public static String[] getElementOptions(final KElement parentElement)
    {
        final VString validElementsVector = parentElement.knownElements();
        final VString uniqueElementsVector  = parentElement.uniqueElements();        
        
        final VString existingElementsVector = parentElement.getElementNameVector(); 
        for (int i = 0; i < existingElementsVector.size(); i++)
        {
            final String existingElementName = existingElementsVector.stringAt(i);
            if (uniqueElementsVector.contains(existingElementName))
            {
                // if element is unique and already in a parentElement - remove it from a valid list
                validElementsVector.remove(existingElementName);
            }
        }
        final int size = validElementsVector.size();
        final String validValues[] = new String[size + 1];
        for (int i = 0; i < size; i++)
        {
            validValues[i] = validElementsVector.get(i).toString();
        }
        validValues[size] = "zzzzzz";
        Arrays.sort(validValues);
        validValues[size] = "Other..";
        return validValues;
    }
////////////////////////////////////////////////////////////////////////////   
    /**
     * Method chooseElementName.
     * gets the valid element to insert into parentElement
     * @param parentElement - 
     * @return the name of the selected element to insert
     */
    public static String chooseElementName(KElement parentElement)
    {
        final String validValues[] = EditorUtils.getElementOptions(parentElement);
        String selectedElementName = (String) JOptionPane.showInputDialog(Editor.getFrame(), "Choose an element to insert",
                "Insert new element", JOptionPane.PLAIN_MESSAGE, null, validValues, validValues[0]);
        
        if (selectedElementName != null && selectedElementName.equals("Other.."))
        {
            selectedElementName = JOptionPane.showInputDialog(Editor.getFrame(), "Choose element name", "");
        }
        return selectedElementName;
    }    

    /**
     * Method getAttributeOptions
     * @param w
     * @return
     */
    public static String[] getAttributeOptions(final KElement element)
    {
        final VString validAttributesVector = element.knownAttributes();
        
        Vector existingAttributes = element.getAttributeVector();
        if (element instanceof JDFResource)
        {
            final JDFResource re = (JDFResource) element; 
            existingAttributes = re.getAttributeVector();
        }
        for (int i = 0; i < existingAttributes.size(); i++)
        {
            final String att = existingAttributes.get(i).toString();
            for (int j = 0; j < validAttributesVector.size(); j++)
            {
                if (att.equals(validAttributesVector.get(j).toString()))
                    validAttributesVector.remove(j);
            }
        }
        final int size = validAttributesVector.size();
        final String possibleValues[] = new String[size + 1];
        for (int i = 0; i < size; i++)
        {
            possibleValues[i] = validAttributesVector.stringAt(i);
        }
        possibleValues[size] = "zzzzzzz";
        Arrays.sort(possibleValues);
        possibleValues[size] = "Other..";
        
        return possibleValues;
    }

    

    /**
     * Method getElement.
     * gets the KElement for this treepath or the parent in case this is an attribute selection
     * @param path
     * @return KElement
     */
    public static KElement getElement(final TreePath path)
    {
        JDFTreeNode node;
        if(path==null)
        {
            node=(JDFTreeNode)Editor.getModel().getRootNode().getFirstChild();
        }
        else
        {
            node = (JDFTreeNode) path.getLastPathComponent();
        }
        
        if(!node.isElement())  // one level is always ok, since attributes live in elements
        {
            node =(JDFTreeNode) node.getParent();
        }
        
        return node.getElement();
    }

    /**
     * checks whether there is a remote chance that the file is useful for reading
     * @param f - File to check
     * @return true if the file is ok
     */
    public static boolean fileOK(File f)
    {
        if (f != null && !f.isDirectory() && f.canRead())
        {
            return true;
        }
        return false;
    }
    /**
     * Creates the String which is to be displayed...
     * @param file the File object to display
     * @param length - The length of the title...
     * @return The file name, may be a little bit altered.
     */
    public static String displayPathName(File file, int length)
    {
        if(file==null)
            return "";
        
        final String s = '"' + file.getAbsolutePath() + '"';
        
        if (s.length() <= 1.5 * length)
            return s;

        final int i = s.indexOf('\\');
        final int j = s.lastIndexOf('\\');
        
        if (i == j)
            return s.substring(0, length - 4) + "..." + '"';
        
        final String start = s.substring(0, i + 1);
        final String end = s.substring(j, s.length());
        
        return start + "..." + end;
    }
    
    /**
     * parse an input stream
     * @param file
     * @return
     * @throws Exception
     */
    public static JDFDoc parseInStream(InputStream inStream) throws Exception
    {
        final JDFParser p = new JDFParser();
        File schemaloc=null;
        final INIReader iniFile=Editor.getIniFile();
        if(iniFile!=null && iniFile.getUseSchema())
        {
            schemaloc=iniFile.getSchemaURL();
            if(schemaloc!=null)
                p.m_SchemaLocation=UrlUtil.fileToUrl(schemaloc, true);  
        }
        JDFDoc jdfDoc = p.parseStream(inStream);
        if(jdfDoc!=null)
        {
            jdfDoc.clearDirtyIDs();
        }
        else if(p.lastExcept!=null) // rethrow the exception tha caused the abort for future display
        {
            throw p.lastExcept;
        }

        return jdfDoc;
    }
 
    //////////////////////////////////////////////////////////////////////////////
    /**
     * creates the standard error box
     * @param errorKey the key in the resources that will be used to display the message
     */
    public static void errorBox(final String errorKey, String addedString)
    {
        final JDFFrame frame = Editor.getFrame();
        if(addedString==null)
            addedString="";
        else
            addedString=" "+addedString;
        
        final ResourceBundle littleBundle = frame.m_littleBundle;
        JOptionPane.showMessageDialog( frame, 
                littleBundle.getString(errorKey)+addedString, 
                littleBundle.getString("ErrorMessKey"),
                JOptionPane.ERROR_MESSAGE);
    }
    //////////////////////////////////////////////////////////////////////////////
    
    /**
     * @param fts the file to get inputstreams from
     * @return InputStream[] the array of inputstreams
     */
    public static EditorDocument[] getEditorDocuments(File fts)
    {
       if(fts==null)
           return null;
       
        EditorDocument ediDocs[]=null;
        try
        {
            String packageName = fts.getCanonicalPath();
            if(packageName.toLowerCase().endsWith(".mjm"))
            {
                ediDocs = unpackMIME(fts, packageName);
            }
            else
            {
                ediDocs=new EditorDocument[1];
                final FileInputStream fileStream=new FileInputStream(fts);
                final EditorDocument edidoc=parseStream(fts, null, fileStream);

                if(edidoc!=null)
                {
                    edidoc.getJDFDoc().setOriginalFileName(fts.getPath());
                    ediDocs[0]=edidoc;
                }
                else
                    ediDocs=null;
            }             
    }
    catch (IOException x)
    {
            return null;
        }
        catch (MessagingException x)
        {
            return null;
        }
        return ediDocs;       
    }
    

    /**
     * unpack the mime packeage specified in fts
     */
    private static EditorDocument[] unpackMIME(File fts, String packageName) throws FileNotFoundException, MessagingException, IOException
    {
        EditorDocument[] ediDocs;
        FileInputStream fileStream=new FileInputStream(fts);
        Multipart mp=MimeUtil.getMultiPart(fileStream);
        if(mp==null)
            return null;
        int count=mp.getCount();
        int n=0;
        // count jdfs and jmfs
        for(int i=0;i<count;i++)
        {
            BodyPart bp=mp.getBodyPart(i);
            if(MimeUtil.isJDFMimeType(bp.getContentType()))
                n++;
        }
        if(n==0)
            return null;
            
        ediDocs=new EditorDocument[n];
        
        n=0;
        for(int i=0;i<count;i++)
        {
            BodyPart bp=mp.getBodyPart(i);
            if(MimeUtil.isJDFMimeType(bp.getContentType()))
            {
                InputStream is=bp.getInputStream();
                final EditorDocument edidoc=parseStream(fts, packageName, is);
                if(edidoc!=null)
                {
                    String fileName = bp.getFileName();
                    if(fileName==null)
                        fileName=packageName+"_"+i+".jdf";
                    final JDFDoc jdfDoc = edidoc.getJDFDoc();
                    jdfDoc.setOriginalFileName(fileName);
                    jdfDoc.setBodyPart(bp);
                    edidoc.setCID(MimeUtil.getContentID(bp));
                    ediDocs[n++]=edidoc;
                }
            }
        }
        return ediDocs;
    }
    
    
    private static EditorDocument parseStream(File fts, String packageName, InputStream is)
    {
        try
        {
            JDFDoc jdfDoc=EditorUtils.parseInStream(is);
            if (jdfDoc!=null)
            {
                final JDFFrame frame = Editor.getFrame();
                frame.setJDFDoc(jdfDoc,packageName);
                return frame.getEditorDoc();
            }
            // refresh the v iew to the selected document
        }
        catch (Exception e)
        {                    
            e.printStackTrace();
            EditorUtils.errorBox("FileNotOpenKey",": "+fts.getName()+"!\n"+e.getMessage()); 
        }
        return null;
    }

}