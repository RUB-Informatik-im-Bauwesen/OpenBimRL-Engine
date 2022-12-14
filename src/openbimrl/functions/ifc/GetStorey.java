package openbimrl.functions.ifc;

import java.util.ArrayList;
import java.util.Collection;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcBuildingStorey;
import com.apstex.ifctoolbox.ifc.IfcElement;
import com.apstex.ifctoolbox.ifc.IfcObjectDefinition;
import com.apstex.ifctoolbox.ifc.IfcRelContainedInSpatialStructure;

import openbimrl.NodeProxy;
import openbimrl.functions.AbstractFunction;

/**
 * Find the IfcStorey that is related to a specific entity.
 * 
 * @author Marcel Stepien
 *
 */
public class GetStorey extends AbstractFunction {

	public GetStorey(NodeProxy nodeProxy) {
		super(nodeProxy);
	}

	@Override
	public void execute(ApplicationModelNode ifcModel) {
		
		Collection<Object> objects = new ArrayList();
		Object input0 = getInput(0);
		if(input0 instanceof Collection<?>) {
			objects = (Collection<Object>)input0;
		}else {
			objects.add(input0);
		}
		
		ArrayList<Object> resultValues = new ArrayList<>();
		
		for(Object o : objects) {
			IfcObjectDefinition.Ifc4 ifcObjectDefinition = (IfcObjectDefinition.Ifc4) o;
			
			if(ifcObjectDefinition instanceof IfcElement) {
				for(IfcRelContainedInSpatialStructure relContains : 
					((IfcElement)ifcObjectDefinition).getContainedInStructure_Inverse()) {
					
					if(relContains instanceof IfcRelContainedInSpatialStructure.Ifc4) {						
						Object spatialElement = ((IfcRelContainedInSpatialStructure.Ifc4)relContains).getRelatingStructure();
						if(spatialElement instanceof IfcBuildingStorey) {
							
							resultValues.add(((IfcBuildingStorey)spatialElement));
							
						}
					}
				}
			}
		
		}
		
		if(resultValues.size()==1) {
			setResult(0, resultValues.get(0));
		}else {
			setResult(0, resultValues);
		}
	}

}
