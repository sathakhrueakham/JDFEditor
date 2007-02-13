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

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.cip4.jdflib.core.JDFElement;
import org.cip4.jdflib.core.JDFRefElement;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.JDFResourceLink.EnumUsage;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.resource.devicecapability.JDFAbstractState;
import org.cip4.jdflib.resource.devicecapability.JDFDevCap;
import org.cip4.jdflib.resource.devicecapability.JDFDevCaps;
import org.w3c.dom.Attr;

/**
 * @author AnderssA ThunellE
 * The treenode in the JTree
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class JDFTreeNode extends DefaultMutableTreeNode
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -2778264565816334126L;
    
    public boolean isInherited;
    
    /**
     * constructor for an element node
     * @param elem the element
     * @param _isValid
     */
    public JDFTreeNode(KElement elem)
    {
        super(elem);
    }
    
    /**
     * constructor for an attribute node
     * @param atr the attribute
     * @param _isValid
     * */
    public JDFTreeNode(Attr atr, boolean _isInherited)
    {
        super(atr);
        this.isInherited = _isInherited;
    }
    
    ///////////////////////////////////////////////////////////////////////
    
    public JDFTreeNode()
    {
        super();
    }
    
    /**
     * true, if either the element or attribute are identical 
     */
    public boolean equals(Object o)
    {
        if(super.equals(o))
            return true;
        if(o==null)
            return false;
        if(!(o instanceof JDFTreeNode))
            return false;
        JDFTreeNode to=(JDFTreeNode)o;
        if(userObject==null)
            return to.getUserObject()==null;
                
        return userObject.equals(to.getUserObject());            
    }
    
    /**
     * return the KElement related to this node. 
     * In case of attribute or text nodes, it is the parent KElement 
     * @return the related element
     */
    public KElement getElement()
    {
        Object o=this.getUserObject();
        if(o instanceof KElement)
            return (KElement) this.getUserObject();
        
        // this is an attribute - try thises parent
        JDFTreeNode nParent=(JDFTreeNode) getParent();
        if(nParent==null)
            return null;
        
        return nParent.getElement();
    }
    
    /**
     * return the Attr related to this node. 
     * In case of attribute or text nodes, it is null 
     * @return the related element
     */
    public Attr getAttr()
    {
        Object o=this.getUserObject();
        if(o instanceof Attr)
            return (Attr)o;
        
        return null;
    }
    
    ///////////////////////////////////////////////////////////////////////
    
    public boolean hasForeignNS()
    {
        final KElement e=getElement();
        return (e!=null) && !(e instanceof JDFElement);
    }
    
    ///////////////////////////////////////////////////////////////////////
    
    public boolean isElement()
    {
        return (userObject instanceof KElement);
    }
    
    ///////////////////////////////////////////////////////////////////////
    
    public boolean isInherited()
    {
        return this.isInherited;   
    }
    
    ///////////////////////////////////////////////////////////////////////
    
    public String getXPath()
    {
        final KElement element = getElement();
        if(element==null)
            return null;
        if(this.isElement())
            return element.buildXPath(null);
        
        return element.buildXPath(null)+"/@"+getName();
    }
    
    /**
     * get the child with name name
     * @param name the name of the child node
     * @return JDFTreeNode the child with name=name
     */
    public JDFTreeNode getNodeWithName(String name)
    {
        if(getChildCount()==0)
            return null;
        JDFTreeNode n=(JDFTreeNode)getFirstChild();
        while(n!=null){
            if(n.getName().equals(name))
                return n;
            n=(JDFTreeNode)n.getNextSibling();
        }
        return n;
    }
    /**
     * get the insert index for a child with name name 
     * always place attributes in front of elements
     * 
     * @param name the name of the child node
     * @param bAttribute if true, the placed element is an attribute, else it is an element
     * @return the index for insertinto
     */
    public int getInsertIndexForName(String name, boolean bAttribute)
    {
        if(getChildCount()==0)
            return -1;
        JDFTreeNode n=(JDFTreeNode)getFirstChild();
        int index=0;
        while(n!=null){
            if(bAttribute && n.isElement())
                return index; // elements are always last
            
            if(n.getName().compareTo(name)>=0){
                if(bAttribute&&!n.isElement()||!bAttribute&&n.isElement())
                    return index;
            }
            n=(JDFTreeNode)n.getNextSibling();
            index++;
        }
        return -1;
    }
    
    ///////////////////////////////////////////////////////////////////////
    
    public String getName()
    {
        if(isElement())
            return getElement().getNodeName();
        if(userObject==null)
            return "";
        return ((Attr)userObject).getNodeName();
    }
    
    ///////////////////////////////////////////////////////////////////////
    
    public String getValue()
    {
        if(isElement())
            return "";
        if(userObject==null)
            return "";
        return ((Attr)userObject).getNodeValue();
    }
    
    ///////////////////////////////////////////////////////////////////////
    
    /**
     * this is the display of the object
     */
    public String toString()
    {
        return toDisplayString();
    }
    
    ///////////////////////////////////////////////////////////////////////
    
    /**
     * this is the display of the object in the tree
     */
    public String toDisplayString()
    {
        Object o=this.getUserObject();
        String s=null;
        if(o==null)
            return null;
        
        if(o instanceof Attr)
        {
            Attr a=(Attr) o;
            s = a.getNodeName()+"=\""+a.getNodeValue()+"\"";        
        }
        else //element
        {
            KElement e=(KElement)o;
            s=e.getNodeName();
            if (e instanceof JDFRefElement)
            {
                final String ref=e.getAttribute("rRef",null,null);
                if(ref!=null)
                    s+=": "+ref;                
            }
            else if (e instanceof JDFResourceLink)
            {
                final JDFResourceLink rl=(JDFResourceLink)e;
                final String ref=rl.getrRef();
                EnumUsage u=rl.getUsage();
                if(EnumUsage.Input.equals(u)|| EnumUsage.Output.equals(u))
                {
                    s+="("+u.getName();  
                }
                final String pu=rl.getProcessUsage();
                if(!pu.equals(""))
                {
                    s+=" ["+pu + "]";
                }
                s+=") : "+ref;   
            }
            else if((e instanceof JDFDevCap) || (e instanceof JDFDevCaps) || (e instanceof JDFAbstractState))
            {
                final String nam=e.getAttribute("Name",null,null);
                if(nam!=null)
                {
                    s+=": "+nam;
                }
            }
            
            // always add id and descname
            final String id=e.getAttribute("ID",null,null);
            if(id!=null)
            {
                s+=", "+id;
            }
         }        
        return s;
    }
    
    ///////////////////////////////////////////////////////////////////////
    
    public String getXPathAttr()
    {
        final KElement element = getElement();
        if(element instanceof JDFNode){
            JDFNode n=(JDFNode)element;
            return n.buildXPath(null);
        }
        return element.getAttribute("XPath");
    }
    
    ///////////////////////////////////////////////////////////////////////
    
    protected String getDCString(String attName, String prefix, String postFix)
    {
        String strValue;
        
        if (!getElement().hasAttribute(attName))
        {
            strValue = "";
        }
        else
        {
            if(prefix==null)
                prefix= " "+attName+"=";
            strValue = prefix + getElement().getAttribute(attName);
            if(postFix!=null)
                strValue+=postFix;
        }
        return strValue;   
    }

    /**
     * get the index of a TreeNode, -1 if null
     * @see javax.swing.tree.DefaultMutableTreeNode#getIndex(javax.swing.tree.TreeNode)
     */
    public int getIndex(TreeNode arg0)
    {
        if(arg0==null)
            return -1;
        return super.getIndex(arg0);
    }
    
    ///////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////
    
}
