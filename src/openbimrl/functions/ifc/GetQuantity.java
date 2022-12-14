package openbimrl.functions.ifc;

import java.util.ArrayList;
import java.util.Collection;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcObjectDefinition;
import com.apstex.ifctoolbox.ifc.IfcPhysicalQuantity;
import com.apstex.ifctoolbox.ifc.IfcQuantityArea;
import com.apstex.ifctoolbox.ifc.IfcQuantityLength;

import openbimrl.NodeProxy;
import openbimrl.functions.AbstractFunction;
import openbimrl.helper.IfcUtilities;

/**
 * Retrieves the quantity value of a specific entity.
 * 
 * @author Marcel Stepien
 *
 */
public class GetQuantity extends AbstractFunction{

	public GetQuantity(NodeProxy nodeProxy) {
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
		
		
		String setName = getInput(1);
		String propertyName = getInput(2);
		
		ArrayList<Object> resultValues = new ArrayList<>();
		
		for(Object o : objects) {
			IfcObjectDefinition.Ifc4 ifcObjectDefinition = (IfcObjectDefinition.Ifc4) o;
				
			IfcPhysicalQuantity physicalQuantity = IfcUtilities.Ifc4.getElementQuantity(ifcObjectDefinition, setName, propertyName);
			
			if(physicalQuantity == null) {
				resultValues.add(null);
			}else if(physicalQuantity instanceof IfcQuantityArea.Ifc4) {
				IfcQuantityArea.Ifc4 quantityArea =
						(IfcQuantityArea.Ifc4) physicalQuantity;
				resultValues.add(quantityArea.getAreaValue().getValue());
			} else if(physicalQuantity instanceof IfcQuantityLength.Ifc4) {
				resultValues.add(((IfcQuantityLength.Ifc4)physicalQuantity).getLengthValue().getValue());
			}
			else {
				
				//FIXME other possibilities
				System.err.println(physicalQuantity.getClassName() + "not covered yet");
				resultValues.add(null);
				
			}
		
		}
		
		if(resultValues.size() == 1) {
			setResult(0, resultValues.get(0));
		}else {
			setResult(0, resultValues);
		}
	}

}
