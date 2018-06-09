/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.script.dom.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArtifact;
import com.archimatetool.model.IBusinessService;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.script.dom.model.DiagramModelObjectProxy.Bounds;

import junit.framework.JUnit4TestAdapter;


/**
 * DiagramModelObjectProxy Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class DiagramModelObjectProxyTests extends DiagramModelComponentProxyTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramModelObjectProxyTests.class);
    }
    
    private DiagramModelObjectProxy actualTestProxy;
    
    private ArchimateModelProxy testModelProxy;
    
    @Before
    public void runOnceBeforeEachTest() {
        testModelProxy = TestsHelper.loadTestModel();
        
        testEObject = (IDiagramModelArchimateObject)ArchimateModelUtils.getObjectByID(testModelProxy.getEObject(), "4104");
        testProxy = EObjectProxy.get(testEObject);
        actualTestProxy = (DiagramModelObjectProxy)testProxy;
    }

    @Test
    public void get_ReturnsCorrectProxy() {
        EObjectProxy proxy = EObjectProxy.get(testEObject);
        assertTrue(proxy instanceof DiagramModelObjectProxy);
        
        proxy = EObjectProxy.get(IArchimateFactory.eINSTANCE.createDiagramModelNote());
        assertTrue(proxy instanceof DiagramModelObjectProxy);
    }

    @Override
    @Test
    public void parent() {
        EObjectProxy parent = testProxy.parent();
        assertEquals("4096", parent.getId());
    }

    @Override
    @Test
    public void parents() {
        EObjectProxyCollection collection = testProxy.parents();
        
        assertEquals(3, collection.size());
        assertEquals("4096", collection.get(0).getId());
        assertEquals("4056", collection.get(1).getId());
        assertEquals("e64e9b49", collection.get(2).getId());
    }
    
    @Override
    @Test
    public void find() {
        EObjectProxyCollection collection = testProxy.find();
        assertEquals(1, collection.size());
    }
    
    @Override
    @Test
    public void find_Selector() {
        super.find_Selector();
        
        EObjectProxyCollection collection = testProxy.find("garbage");
        assertEquals(0, collection.size());

        collection = testProxy.find("*");
        assertEquals(1, collection.size());
    }

    @Test
    public void setArchimateConcept() {
        assertTrue(actualTestProxy.getArchimateConcept().getEObject() instanceof IBusinessService);
        
        IArtifact element = IArchimateFactory.eINSTANCE.createArtifact();
        ArchimateElementProxy elementProxy = new ArchimateElementProxy(element);
        actualTestProxy.setArchimateConcept(elementProxy);
        
        assertTrue(actualTestProxy.getArchimateConcept().getEObject() == element);
    }

    @Test
    public void getArchimateConcept() {
        assertTrue(actualTestProxy.getArchimateConcept().getEObject() instanceof IBusinessService);
        IDiagramModelGroup group = (IDiagramModelGroup)ArchimateModelUtils.getObjectByID(testModelProxy.getEObject(), "4096");
        DiagramModelObjectProxy groupProxy = new DiagramModelObjectProxy(group);
        assertNull(groupProxy.getArchimateConcept());
    }
    
    @Override
    @Test
    public void getReferencedConcept() {
        assertSame(actualTestProxy.getArchimateConcept().getEObject(), actualTestProxy.getReferencedConcept());
        
        // Group has none
        IDiagramModelGroup group = (IDiagramModelGroup)ArchimateModelUtils.getObjectByID(testModelProxy.getEObject(), "4096");
        DiagramModelObjectProxy groupProxy = new DiagramModelObjectProxy(group);
        assertSame(groupProxy.getEObject(), groupProxy.getReferencedConcept());
        
        // Diagram model reference
        IDiagramModelReference ref = (IDiagramModelReference)ArchimateModelUtils.getObjectByID(testModelProxy.getEObject(), "3657");
        DiagramModelObjectProxy refProxy = new DiagramModelObjectProxy(ref);
        assertSame(ref.getReferencedModel(), refProxy.getReferencedConcept());

    }
    
    @Test
    public void outRels() {
        EObjectProxyCollection collection = actualTestProxy.outRels();
        assertEquals(1, collection.size());
        for(EObjectProxy eObjectProxy : collection) {
            assertTrue(eObjectProxy instanceof DiagramModelConnectionProxy);
        }
    }

    @Test
    public void inRels() {
        EObjectProxyCollection collection = actualTestProxy.inRels();
        assertEquals(1, collection.size());
        for(EObjectProxy eObjectProxy : collection) {
            assertTrue(eObjectProxy instanceof DiagramModelConnectionProxy);
        }
    }
    
    @Test
    public void getBounds() {
        Bounds bounds = actualTestProxy.getBounds();
        assertEquals(20, bounds.x);
        assertEquals(25, bounds.y);
        assertEquals(101, bounds.width);
        assertEquals(60, bounds.height);
    }
    
    @Override
    @Test
    public void children() {
        super.children();
        
        IDiagramModelObject dmo = (IDiagramModelObject)ArchimateModelUtils.getObjectByID(testModelProxy.getEObject(), "3707");
        DiagramModelObjectProxy proxy = new DiagramModelObjectProxy(dmo);
        
        EObjectProxyCollection collection = proxy.children();
        assertEquals(6, collection.size());
        
        for(EObjectProxy eObjectProxy : collection) {
            assertTrue(eObjectProxy instanceof DiagramModelObjectProxy);
        }
    }
    
    @Override
    @Test
    public void attr_Get() {
        super.attr_Get();
        
        IDiagramModelObject dmo = (IDiagramModelObject)ArchimateModelUtils.getObjectByID(testModelProxy.getEObject(), "3707");
        DiagramModelObjectProxy proxy = new DiagramModelObjectProxy(dmo);
        
        assertEquals(null, proxy.attr(IModelConstants.FONT_COLOR));
        assertEquals("1|Arial|14.0|0|WINDOWS|1|0|0|0|0|0|0|0|0|1|0|0|0|0|Arial", proxy.attr(IModelConstants.FONT));
        assertEquals(null, proxy.attr(IModelConstants.LINE_COLOR));
        assertEquals(1, proxy.attr(IModelConstants.LINE_WIDTH));
        assertEquals("#ffff80", proxy.attr(IModelConstants.FILL_COLOR));
        
        Bounds bounds = (Bounds)proxy.attr(IModelConstants.BOUNDS);
        assertEquals(20, bounds.x);
        assertEquals(20, bounds.y);
        assertEquals(440, bounds.width);
        assertEquals(500, bounds.height);
    }

    @Override
    @Test
    public void delete() {
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        
        IDiagramModelGroup group1 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dm.getChildren().add(group1);
        IDiagramModelGroup group2 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dm.getChildren().add(group2);
        IDiagramModelGroup group3 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        group2.getChildren().add(group3);
        IDiagramModelGroup group4 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        group3.getChildren().add(group4);

        IDiagramModelConnection connection1 = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        IDiagramModelConnection connection2 = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        IDiagramModelConnection connection3 = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        IDiagramModelConnection connection4 = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        IDiagramModelConnection connection5 = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        IDiagramModelConnection connection6 = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        IDiagramModelConnection connection7 = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        
        connection1.connect(group1, group2);
        connection2.connect(group3, connection1);
        connection3.connect(connection1, group4);
        connection4.connect(connection2, group4);
        connection5.connect(group3, connection3);
        connection6.connect(group3, connection4);
        connection7.connect(connection5, group4);
        
        assertSame(connection1, group1.getSourceConnections().get(0));
        assertEquals(1, connection1.getSourceConnections().size());
        assertEquals(1, connection1.getTargetConnections().size());

        DiagramModelObjectProxy proxy1 = (DiagramModelObjectProxy)EObjectProxy.get(group1);
        proxy1.delete();
        
        DiagramModelObjectProxy proxy2 = (DiagramModelObjectProxy)EObjectProxy.get(group2);
        proxy2.delete();
        
        assertNull(group1.eContainer());
        assertNull(group2.eContainer());
        assertNull(group3.eContainer());
        assertNull(group4.eContainer());
        
        assertTrue(group1.getSourceConnections().isEmpty());
        assertTrue(group1.getTargetConnections().isEmpty());
        assertTrue(group2.getSourceConnections().isEmpty());
        assertTrue(group2.getTargetConnections().isEmpty());
        assertTrue(group3.getSourceConnections().isEmpty());
        assertTrue(group3.getTargetConnections().isEmpty());
        assertTrue(group4.getSourceConnections().isEmpty());
        assertTrue(group4.getTargetConnections().isEmpty());

        assertTrue(connection1.getSourceConnections().isEmpty());
        assertTrue(connection1.getTargetConnections().isEmpty());
        assertTrue(connection2.getSourceConnections().isEmpty());
        assertTrue(connection2.getTargetConnections().isEmpty());
        assertTrue(connection3.getSourceConnections().isEmpty());
        assertTrue(connection3.getTargetConnections().isEmpty());
        assertTrue(connection4.getSourceConnections().isEmpty());
        assertTrue(connection4.getTargetConnections().isEmpty());
        assertTrue(connection5.getSourceConnections().isEmpty());
        assertTrue(connection5.getTargetConnections().isEmpty());
        assertTrue(connection6.getSourceConnections().isEmpty());
        assertTrue(connection6.getTargetConnections().isEmpty());
        assertTrue(connection7.getSourceConnections().isEmpty());
        assertTrue(connection7.getTargetConnections().isEmpty());
    }
}