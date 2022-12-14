package openbimrl.functions.ifc;

import java.util.ArrayList;
import java.util.Collection;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcElement;
import com.apstex.ifctoolbox.ifc.IfcFeatureElement;
import com.apstex.ifctoolbox.ifc.IfcRelFillsElement;
import com.apstex.ifctoolbox.ifc.IfcRelVoidsElement;

import openbimrl.NodeProxy;
import openbimrl.functions.AbstractFunction;

/**
 * Retrieves the opening elements as features of an IFC element.
 * 
 * @author Marcel Stepien
 *
 */
public class GetOpeningElements extends AbstractFunction {

	public GetOpeningElements(NodeProxy nodeProxy) {
		super(nodeProxy);
	}

	@Override
	public void execute(ApplicationModelNode ifcModel) {
	
		Object input0 = getInput(0);
		
		if(input0 == null)
			return;
				
		Collection<?> objects = null;
		if(input0 instanceof Collection<?>) {
			objects = (Collection<?>) input0;
		}else {
			ArrayList<Object> newList = new ArrayList<Object>();
			newList.add(input0);
			objects = newList;
		}
		
		ArrayList<IfcFeatureElement> related = new ArrayList<IfcFeatureElement>();
		ArrayList<IfcFeatureElement> relating = new ArrayList<IfcFeatureElement>();
		
		for(Object o : objects) {
			if(o instanceof IfcElement) {
				IfcElement oEle = (IfcElement)o;
				
				if(oEle.getHasOpenings_Inverse() != null) {
					for(IfcRelVoidsElement vRel : oEle.getHasOpenings_Inverse()) {
						related.add((IfcFeatureElement)vRel.getRelatedOpeningElement());
					}
				}
				
				if(oEle.getFillsVoids_Inverse() != null) {
					for(IfcRelFillsElement fRel : oEle.getFillsVoids_Inverse()) {
						relating.add((IfcFeatureElement)fRel.getRelatingOpeningElement());
					}
				}
			}
		}
		
		if(related.size()==1) {
			setResult(0, related.get(0));
		} else {
			setResult(0, related);
		}

		if(relating.size()==1) {
			setResult(1, relating.get(0));
		} else {
			setResult(1, relating);
		}
	}

}
