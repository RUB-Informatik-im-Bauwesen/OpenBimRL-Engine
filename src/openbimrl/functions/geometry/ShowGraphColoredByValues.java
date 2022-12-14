package openbimrl.functions.geometry;

import java.awt.Color;
import java.util.HashMap;

import org.apache.commons.geometry.euclidean.threed.Vector3D;

import com.apstex.gui.core.j3d.model.cadobjectmodel.CadObjectJ3D;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.j3d.utils.geometry.Sphere;
import com.apstex.javax.media.j3d.Appearance;
import com.apstex.javax.media.j3d.BranchGroup;
import com.apstex.javax.media.j3d.ColoringAttributes;
import com.apstex.javax.media.j3d.LineAttributes;
import com.apstex.javax.media.j3d.Transform3D;
import com.apstex.javax.media.j3d.TransformGroup;
import com.apstex.javax.vecmath.Color3f;
import com.apstex.javax.vecmath.Vector3f;

import openbimrl.NodeProxy;
import openbimrl.functions.AbstractFunction;

/**
 * Loads the graph information and displays it in the 3D viewer.
 * 
 * @author Marcel Stepien
 *
 */
public class ShowGraphColoredByValues extends AbstractFunction {
	
	public ShowGraphColoredByValues(NodeProxy nodeProxy) {
		super(nodeProxy);
	}

	@Override
	public void execute(ApplicationModelNode ifcModel) {
	
		Object input0 = getInput(0);
		
		if(input0 == null)
			return;
				
		HashMap<Vector3D, Double> nodeValues = new HashMap<Vector3D, Double>();
		if(input0 instanceof HashMap) {
			nodeValues = (HashMap)input0;
		}else {
			return;
		}
				
		BranchGroup group = new BranchGroup();
		for(Vector3D node : nodeValues.keySet()) {
			Double value = nodeValues.get(node);
			handleNodes(node, value, group);
		}
		
		CadObjectJ3D objectJ3D = new CadObjectJ3D();
		objectJ3D.addChild(group);
		
		//Add to viewer and shows geometry
		((CadObjectJ3D)ifcModel.getCadObjectModel().getRootBranchGroup()).addChild(objectJ3D);

		setResult(0, true);
		
	}
	
	private void handleNodes(Vector3D node, double distance, BranchGroup group) {
		int g = 255 - (int)(255 * (distance/35.0));
		if(g > 255) { g = 255; }
		if(g < 0) { g = 0; }
			
		Appearance appearance = createAppearance(new Color(255 - g, g, 0), 1.0f);
		showGraphNodes(node, group, appearance);
	}
	
	private void showGraphNodes(Vector3D node, BranchGroup group, Appearance appearance){
		Sphere pointShape = new Sphere(0.1f, appearance);
		
		TransformGroup tg = new TransformGroup();
		Transform3D transform = new Transform3D();
		Vector3f vector = new Vector3f(
				(float)node.getX(), 
				(float)node.getY(), 
				(float)node.getZ()
		);
		transform.setTranslation(vector);
		tg.setTransform(transform);
		tg.addChild(pointShape);
		
		group.addChild(tg);
				
	}

	private Appearance createAppearance(Color color, float lineThickness) {
		Appearance appearance = new Appearance();
		ColoringAttributes coloringAttributes = new ColoringAttributes(
				new Color3f(color), 
				ColoringAttributes.FASTEST
		);
		
		LineAttributes lineAttributes = new LineAttributes();
		lineAttributes.setLineWidth(lineThickness);
		
		appearance.setLineAttributes(lineAttributes);
		appearance.setColoringAttributes(coloringAttributes);
		return appearance;
	}

}
