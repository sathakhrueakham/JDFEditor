package org.cip4.jdfeditor;

/*
 *
 * The CIP4 Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2009 The International Cooperation for the Integration of 
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
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "CIP4" and "The International Cooperation for the Integration of 
 *    Processes in  Prepress, Press and Postpress" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written 
 *    permission, please contact info@cip4.org.
 *
 * 5. Products derived from this software may not be called "CIP4",
 *    nor may "CIP4" appear in their name, without prior written
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
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
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
 * originally based on software 
 * copyright (c) 1999-2001, Heidelberger Druckmaschinen AG 
 * copyright (c) 1999-2001, Agfa-Gevaert N.V. 
 *  
 * For more information on The International Cooperation for the 
 * Integration of Processes in  Prepress, Press and Postpress , please see
 * <http://www.cip4.org/>.
 *  
 * 
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.cip4.jdflib.core.ElementName;
import org.cip4.jdflib.core.JDFConstants;
import org.cip4.jdflib.core.JDFResourceLink;
import org.cip4.jdflib.core.KElement;
import org.cip4.jdflib.core.VElement;
import org.cip4.jdflib.node.JDFNode;
import org.cip4.jdflib.pool.JDFResourceLinkPool;
import org.cip4.jdflib.resource.JDFResource;

public class JDFInOutScroll extends JScrollPane
{

	/**
     * 
     */
	private static final long serialVersionUID = 8635330186484361532L;
	JPanel m_inOutArea;
	private final JPanel m_inOutAreaLeft;
	private final JPanel m_inOutAreaMiddle;
	private final JPanel m_inOutAreaRight;
	private JDFTreeNode m_searchInOutNode;
	private int m_inTreePos = 0;
	private int m_outTreePos = 0;

	public JDFInOutScroll()
	{
		super();
		m_inOutArea = new JPanel();
		m_inOutArea.setLayout(null);
		m_inOutArea.setBackground(Color.white);

		m_inOutAreaLeft = new JPanel();
		m_inOutAreaLeft.setLayout(null);
		m_inOutAreaLeft.setBackground(Color.white);
		m_inOutArea.add(m_inOutAreaLeft);

		m_inOutAreaMiddle = new JPanel();
		m_inOutAreaMiddle.setLayout(null);
		m_inOutAreaMiddle.setBackground(Color.white);
		m_inOutArea.add(m_inOutAreaMiddle);

		m_inOutAreaRight = new JPanel();
		m_inOutAreaRight.setLayout(null);
		m_inOutAreaRight.setBackground(Color.white);
		m_inOutArea.add(m_inOutAreaRight);

		m_inOutArea.add(Box.createHorizontalGlue());
		getViewport().add(m_inOutArea, null);
		getVerticalScrollBar().setUnitIncrement(20);
		getHorizontalScrollBar().setUnitIncrement(20);
	}

	/**
	 * 
	 * Draws the In & Output View Area with its components, except for the Input/JDF Producer and Output/JDF Consumer Trees. *
	 * @param element The element you wish to draw in the In & Output View
	 * @param lStr The Left Title
	 * @param mStr The Middle Title
	 * @param rStr The Right Title
	 */
	private void drawInOutView(final KElement element, final String lStr, final String mStr, final String rStr)
	{
		final Dimension d = getSize();
		final int w = d.width / 3;

		m_inOutAreaLeft.add(getTitleLabel(lStr, w));
		m_inOutAreaMiddle.add(getTitleLabel(mStr, w));
		m_inOutAreaRight.add(getTitleLabel(rStr, w));

		final JTree mTree = getInOutNodes(element);
		ToolTipManager.sharedInstance().registerComponent(mTree);
		mTree.setShowsRootHandles(false);
		m_inOutAreaMiddle.add(mTree);
		final int mHeight = mTree.getPreferredSize().height;
		mTree.setBounds(5, 50, w - 10, mHeight);

		m_inOutAreaLeft.setBounds(0, 0, w, m_inTreePos);
		m_inOutAreaMiddle.setBounds(w, 0, w, mHeight + 50);
		m_inOutAreaRight.setBounds(2 * w, 0, w, m_outTreePos);
		final int h = m_inTreePos < m_outTreePos ? m_outTreePos : m_inTreePos;
		final Dimension dim = new Dimension(d.width - 20, h < mHeight + 50 ? mHeight + 50 : h);
		m_inOutArea.setPreferredSize(dim);

		m_inOutArea.repaint();
		getViewport().add(m_inOutArea, null);
		final JDFFrame m_frame = Editor.getFrame();
		final EditorTabbedPaneA editorTabbedPaneA = m_frame.m_topTabs;
		editorTabbedPaneA.setComponentAt(editorTabbedPaneA.m_IO_INDEX, this);
		editorTabbedPaneA.setSelectedIndex(editorTabbedPaneA.m_IO_INDEX);
	}

	/**
	 * Creates a JLabel for the titles in the In & Output View
	 * @param String - The text on the JLabel
	 * @param int - The width of the JLabel
	 * @return A JLabel with the title text
	 */
	private JLabel getTitleLabel(final String title, final int width)
	{
		final JLabel label = new JLabel(title);
		label.setFont(new Font("Verdana", Font.BOLD, 12));
		label.setBackground(Color.white);
		label.setForeground(Color.black);
		label.setBounds(5, 0, width, 50);

		return label;
	}

	/**
	 * Adds the Input/JDF Producer and Output/JDF Consumer Trees to the In & Output View. Called numerous times to draw all the different trees
	 * @param elem - The element you want to add as a Tree to the In & Output View
	 * @param isJDFElem - Is the element a JDF element?
	 */
	private void addResourceTree(final KElement elem, final boolean isJDFElem)
	{
		final Dimension d = getSize();
		final int w = d.width / 3;
		final String usage = elem.getAttribute("Usage", null, "");
		final String rRef = elem.getAttribute("rRef", null, "");

		KElement res = null;
		if (isJDFElem)
		{
			res = elem.getTarget_KElement(rRef, "ID");
		}
		else
		{
			res = (KElement) elem.getParentNode().getParentNode();
		}

		if (usage.equals("Input") == isJDFElem)
		{
			final JTree inTree = getInOutNodes(res);
			m_inOutAreaLeft.add(inTree);
			ToolTipManager.sharedInstance().registerComponent(inTree);
			inTree.setBounds(5, m_inTreePos, w - 10, inTree.getPreferredSize().height);
			m_inTreePos += inTree.getPreferredSize().height + 10;
		}
		if (usage.equals("Output") == isJDFElem)
		{
			final JTree outTree = getInOutNodes(res);
			m_inOutAreaRight.add(outTree);
			ToolTipManager.sharedInstance().registerComponent(outTree);
			outTree.setBounds(5, m_outTreePos, w - 10, outTree.getPreferredSize().height);
			m_outTreePos += outTree.getPreferredSize().height + 10;
		}
	}

	/**
	 * Searching after a string in the nextneigbour view, starting from the selected node.
	 * @param inString - The String to search for
	 * @param forwardDirection - Search forward or backward?
	 * @param bIgnoreCase
	 */
	public void findStringInNeighbourTree(final String inString, final boolean forwardDirection, final boolean bIgnoreCase)
	{
		Editor.setCursor(1, null);
		final JDFFrame m_frame = Editor.getFrame();
		if (m_frame.m_searchTree != null && m_searchInOutNode != null && inString != null && !inString.equals(JDFConstants.EMPTYSTRING))
		{
			boolean found = false;
			String searchString = inString;
			if (bIgnoreCase)
			{
				searchString = searchString.toUpperCase();
			}
			final JPanel[] areaArray = { m_inOutAreaLeft, m_inOutAreaMiddle, m_inOutAreaRight };
			JPanel areaPanel;
			boolean finishedFirstSearch = false;
			JTree lastSelectedTree = m_frame.m_searchTree;

			if (forwardDirection)
			{
				for (int j = 0; j < areaArray.length; j++)
				{
					areaPanel = areaArray[j];
					final int count = areaPanel.getComponentCount() - 1;

					for (int i = 0; i < count; i++)
					{

						final Component component2 = areaPanel.getComponent(i);
						if (!(component2 instanceof JTree))
						{
							continue;
						}

						final JTree tmpTree = (JTree) component2;

						if (finishedFirstSearch)
						{
							m_frame.m_searchTree = tmpTree;
							m_searchInOutNode = (JDFTreeNode) (tmpTree.getPathForRow(0)).getLastPathComponent();
						}
						if (tmpTree.equals(m_frame.m_searchTree))
						{
							if (((JDFTreeNode) (tmpTree.getPathForRow(0)).getLastPathComponent()).equals(m_searchInOutNode))
							{
								final Enumeration e = m_searchInOutNode.preorderEnumeration();
								Object currNode;

								if (!finishedFirstSearch)
								{
									e.nextElement();
								}

								while (e.hasMoreElements())
								{
									currNode = e.nextElement();
									final JDFTreeNode checkNode = (JDFTreeNode) currNode;
									String tmpString = checkNode.toString();
									if (bIgnoreCase)
									{
										tmpString = tmpString.toUpperCase();
									}

									if (tmpString.indexOf(searchString) != -1)
									{
										lastSelectedTree.removeSelectionPath(lastSelectedTree.getSelectionPath());
										m_searchInOutNode = checkNode;
										m_frame.m_searchTree = tmpTree;
										((JLabel) ((Box) ((Box) m_frame.m_dialog.getContentPane().getComponent(1)).getComponent(7)).getComponent(1)).setText(" ");
										final TreePath path = new TreePath(checkNode.getPath());
										lastSelectedTree = tmpTree;
										tmpTree.makeVisible(path);
										tmpTree.setSelectionPath(path);
										tmpTree.scrollPathToVisible(path);
										found = true;
										break;
									}
								}
							}
							else
							{
								JDFTreeNode checkNode = m_searchInOutNode;

								while (checkNode.getNextSibling() != null)
								{
									checkNode = (JDFTreeNode) checkNode.getNextSibling();
									String tmpString = checkNode.toString();
									if (bIgnoreCase)
									{
										tmpString = tmpString.toUpperCase();
									}
									if (tmpString.indexOf(searchString) != -1)
									{
										lastSelectedTree.removeSelectionPath(lastSelectedTree.getSelectionPath());
										m_searchInOutNode = checkNode;
										m_frame.m_searchTree = tmpTree;
										((JLabel) ((Box) ((Box) m_frame.m_dialog.getContentPane().getComponent(1)).getComponent(7)).getComponent(1)).setText(" ");
										final TreePath path = new TreePath(checkNode.getPath());
										lastSelectedTree = tmpTree;
										tmpTree.makeVisible(path);
										tmpTree.setSelectionPath(path);
										tmpTree.scrollPathToVisible(path);
										found = true;
										break;
									}
								}
							}
							if (found)
							{
								break;
							}

							finishedFirstSearch = true;
						}
					}
					if (found)
					{
						break;
					}
				}
			}
			else
			{
				boolean lastWasRoot = false;
				for (int j = areaArray.length - 1; j >= 0; j--)
				{
					areaPanel = areaArray[j];
					final int nr = areaPanel.getComponentCount() - 1;
					final int count = 0;

					for (int i = nr; i >= count; i--)
					{
						final Component component2 = areaPanel.getComponent(i);
						if (!(component2 instanceof JTree))
						{
							continue;
						}

						final JTree tmpTree = (JTree) component2;

						if (finishedFirstSearch)
						{
							m_frame.m_searchTree = tmpTree;
							final JDFTreeNode tmpNode = (JDFTreeNode) (tmpTree.getPathForRow(0)).getLastPathComponent();
							if (tmpNode.getChildCount() != 0)
							{
								m_searchInOutNode = (JDFTreeNode) tmpNode.getLastChild();
							}
							else
							{
								m_searchInOutNode = tmpNode;
							}
						}
						if (tmpTree.equals(m_frame.m_searchTree))
						{
							if (!((JDFTreeNode) (tmpTree.getPathForRow(0)).getLastPathComponent()).equals(m_searchInOutNode))
							{
								final Enumeration e = ((JDFTreeNode) (tmpTree.getPathForRow(0)).getLastPathComponent()).preorderEnumeration();
								final Stack tmpStack = new Stack();

								while (e.hasMoreElements())
								{
									tmpStack.push(e.nextElement());
								}
								JDFTreeNode checkNode;
								while (!tmpStack.isEmpty())
								{
									checkNode = m_searchInOutNode;

									if (!lastWasRoot)
									{
										checkNode = (JDFTreeNode) tmpStack.pop();
									}

									if (checkNode.equals(m_searchInOutNode))
									{
										while (!tmpStack.isEmpty())
										{
											checkNode = (JDFTreeNode) tmpStack.pop();
											String tmpString = checkNode.toString();
											if (bIgnoreCase)
											{
												tmpString = tmpString.toUpperCase();
											}

											if (tmpString.indexOf(searchString) != -1)
											{
												lastSelectedTree.removeSelectionPath(lastSelectedTree.getSelectionPath());
												m_searchInOutNode = checkNode;
												((JLabel) ((Box) ((Box) m_frame.m_dialog.getContentPane().getComponent(1)).getComponent(7)).getComponent(1)).setText(" ");
												final TreePath path = new TreePath(checkNode.getPath());
												lastSelectedTree = tmpTree;
												tmpTree.makeVisible(path);
												tmpTree.setSelectionPath(path);
												tmpTree.scrollPathToVisible(path);
												found = true;
												break;
											}
										}
										if (found)
										{
											break;
										}
									}
								}
								lastWasRoot = false;
							}
							else
							{
								lastWasRoot = true;
							}

							finishedFirstSearch = true;
						}
						if (found)
						{
							break;
						}
					}
					if (found)
					{
						break;
					}
				}
			}
			if (!found)
			{
				((JLabel) ((Box) ((Box) m_frame.m_dialog.getContentPane().getComponent(1)).getComponent(7)).getComponent(1)).setText(Editor.getBundle().getString("StringNotFoundKey"));
			}
		}
		Editor.setCursor(0, null);
	}

	public JTree findIt()
	{
		boolean found = false;
		JTree searchTree = null;
		if (!found)
		{
			for (int i = 0; i < m_inOutAreaLeft.getComponentCount() && !found; i++)
			{
				final Component comp = m_inOutAreaLeft.getComponent(i);
				if (comp instanceof JTree && comp.isFocusOwner())
				{
					searchTree = (JTree) comp;
					m_searchInOutNode = (JDFTreeNode) ((JTree) comp).getSelectionPath().getLastPathComponent();
					found = true;
				}
			}
		}
		if (!found)
		{
			if (m_inOutAreaMiddle.getComponentCount() > 1)
			{
				final Component comp = m_inOutAreaMiddle.getComponent(1);
				if (comp instanceof JTree && comp.isFocusOwner())
				{
					searchTree = (JTree) comp;
					m_searchInOutNode = (JDFTreeNode) ((JTree) comp).getSelectionPath().getLastPathComponent();
					found = true;
				}
			}
		}
		if (!found)
		{
			for (int i = 0; i < m_inOutAreaRight.getComponentCount() && !found; i++)
			{
				final Component comp = m_inOutAreaRight.getComponent(i);
				if (comp instanceof JTree && comp.isFocusOwner())
				{
					searchTree = (JTree) comp;
					m_searchInOutNode = (JDFTreeNode) ((JTree) comp).getSelectionPath().getLastPathComponent();
					found = true;
				}
			}
		}
		return searchTree;
	}

	/**
	 * clear the input ouput view
	 * 
	 */
	public void clearInOutView()
	{
		m_inOutAreaLeft.removeAll();
		m_inOutAreaRight.removeAll();
		m_inOutAreaMiddle.removeAll();
		m_inOutArea.validate();
		m_inOutArea.repaint();
	}

	/**
	 * Creates the In & Output View.
	 */
	public void initInOutView(EditorDocument eDoc)
	{
		if (eDoc == null)
		{
			eDoc = Editor.getEditorDoc();
		}

		if (eDoc == null)
		{
			return;
		}

		Editor.setCursor(1, null);
		final TreePath path = eDoc.getSelectionPath();

		JDFTreeNode node = null;
		final JDFFrame m_frame = Editor.getFrame();

		if (path != null)
		{
			node = (JDFTreeNode) path.getLastPathComponent();
		}
		else if (m_frame.m_treeArea != null)
		{
			final JDFTreeNode rootNode = eDoc.getRootNode();
			node = rootNode == null ? null : (JDFTreeNode) rootNode.getFirstChild();
		}
		if (node == null)
		{
			return;
		}

		eDoc.setSelectionPath(new TreePath(node.getPath()), true);

		final KElement root = eDoc.getJDFDoc().getRoot(); // check whether JMF,
		if (node.isElement() && (root instanceof JDFNode))
		{
			final KElement kElement = node.getElement();
			m_inTreePos = m_outTreePos = 50;
			SwingUtilities.updateComponentTreeUI(this);

			if (kElement != null)
			{

				String lTitle = "";
				String mTitle = "";
				String rTitle = "";
				boolean isJDFNode = false;

				if (kElement instanceof JDFNode)
				{
					final ResourceBundle m_littleBundle = Editor.getBundle();
					mTitle = m_littleBundle.getString("JDFElementKey");
					isJDFNode = true;
					final JDFNode n = (JDFNode) kElement;

					if (kElement.hasChildElement(ElementName.RESOURCELINKPOOL, null))
					{
						final JDFResourceLinkPool resourceLinkPool = n.getResourceLinkPool();
						if (resourceLinkPool != null && resourceLinkPool.hasChildNodes())
						{
							final VElement resourceLinks = resourceLinkPool.getPoolChildren(null, null, null);

							lTitle = m_littleBundle.getString("InputResourceKey");
							rTitle = m_littleBundle.getString("OutputResourceKey");

							for (int i = 0; i < resourceLinks.size(); i++)
							{
								final JDFResourceLink link = (JDFResourceLink) resourceLinks.item(i);
								if (link.getTarget() != null)
								{
									addResourceTree(link, isJDFNode);
								}
							}
						}
					}
					drawInOutView(kElement, lTitle, mTitle, rTitle);
				}
				else if (kElement instanceof JDFResource && (kElement.hasChildElements() || kElement.hasAttributes()))
				{
					final JDFResource r = (JDFResource) kElement;

					String id = r.getID();
					if (id.equals(JDFConstants.EMPTYSTRING))
					{
						id = r.getResourceRoot().getID();
					}
					final ResourceBundle m_littleBundle = Editor.getBundle();

					mTitle = m_littleBundle.getString("ResourceKey");
					Vector vProcs = new Vector();
					if (root instanceof JDFNode)
					{
						vProcs = ((JDFNode) root).getvJDFNode(null, null, false);
					}

					for (int i = 0; i < vProcs.size(); i++)
					{
						final JDFNode jdfNode = (JDFNode) vProcs.elementAt(i);
						if (jdfNode.hasChildElement(ElementName.RESOURCELINKPOOL, null))
						{
							rTitle = m_littleBundle.getString("JDFConsumerKey");
							lTitle = m_littleBundle.getString("JDFProducerKey");

							final JDFResourceLinkPool rlp = jdfNode.getResourceLinkPool();
							if (rlp != null)
							{
								final VElement resourceLinks = rlp.getPoolChildren(null, null, null);
								if (resourceLinks != null)
								{
									final int size = resourceLinks.size();
									for (int j = 0; j < size; j++)
									{
										final JDFResourceLink link = (JDFResourceLink) resourceLinks.elementAt(j);
										if (link.getLinkRoot() == r)
										{
											addResourceTree(link, isJDFNode);
										}
									}
								}
							}
						}
					}
					drawInOutView(kElement, lTitle, mTitle, rTitle);
				}
				else
				{
					mTitle = kElement.getLocalName();
					drawInOutView(kElement, lTitle, mTitle, rTitle);
				}
			}
		}
		Editor.setCursor(0, null);

	}

	/**
	 * Creates the Trees from the Nodes in the In & Output View
	 * @param rootElement - The element you want to create a Tree from
	 * @return The JTree
	 */
	private JTree getInOutNodes(final KElement rootElement)
	{
		final JDFTreeNode mRoot = new JDFTreeNode(rootElement);
		final JTree resTree = new JTree(mRoot);
		final JDFTreeModel treeModel = new JDFTreeModel(mRoot, true);
		treeModel.addNodeAttributes(mRoot);
		resTree.setModel(treeModel);

		final MouseAdapter mouseListener = new MouseAdapter()
		{
			@Override
			public void mouseClicked(final MouseEvent e)
			{
				final JTree tree = (JTree) e.getSource();

				if (SwingUtilities.isLeftMouseButton(e) && !e.isControlDown())
				{
					final JDFFrame m_frame = Editor.getFrame();

					final TreePath path = tree.getSelectionPath();
					if (path != null)
					{
						m_frame.m_treeArea.findNode((JDFTreeNode) path.getLastPathComponent());
					}
				}
				else if (SwingUtilities.isRightMouseButton(e) || e.isControlDown())
				{
					final TreePath path = tree.getPathForLocation(e.getX(), e.getY());
					if (path != null)
					{
						tree.setSelectionRow(tree.getRowForPath(path));
						final JDFTreeNode node = (JDFTreeNode) path.getLastPathComponent();

						if (node.isElement())
						{
							final JDFFrame m_frame = Editor.getFrame();

							m_frame.m_treeArea.findNode(node);
							clearInOutView();
							initInOutView(null);
						}
					}
				}
			}
		};
		resTree.addMouseListener(mouseListener);
		ToolTipManager.sharedInstance().registerComponent(resTree);
		resTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		final JDFResourceRenderer resourceRenderer = new JDFResourceRenderer();
		resTree.setCellRenderer(resourceRenderer);
		resTree.setRowHeight(18);
		resTree.setBackground(Color.white);

		return resTree;
	}

}
