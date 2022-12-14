package openbimrl.functions.ifc;

import java.util.ArrayList;
import java.util.Collection;

import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcCartesianPoint;
import com.apstex.ifctoolbox.ifc.IfcPolyline;
import com.apstex.javax.vecmath.Point3d;

import openbimrl.NodeProxy;
import openbimrl.functions.AbstractFunction;

/**
 * Calculates the length of a Curve (like an IfcPolyline).
 * 
 * @author Marcel Stepien
 *
 */
public class GetCurveLength extends AbstractFunction {
	
	public GetCurveLength(NodeProxy nodeProxy) {
		super(nodeProxy);
	}

	@Override
	public void execute(ApplicationModelNode ifcModel) {
	
		Object input0 = getInput(0);
		
		if(input0 == null)
			return;
		
		Collection<?> polylines = null;
		if(input0 instanceof Collection<?>) {
			polylines = (Collection<?>) input0;
		}else {
			ArrayList<Object> newList = new ArrayList<Object>();
			newList.add(input0);
			polylines = newList;
		}

		ArrayList<Double> resultValues = new ArrayList<Double>();
		
		for(Object repItemObj : polylines) {
			if(repItemObj instanceof IfcPolyline) {
				
				double distanceMeasured = 0.0;
				boolean first = true;
				Point3d previouse = null;
				
				for(IfcCartesianPoint point : ((IfcPolyline)repItemObj).getPoints()) {
					Point3d currentP = new Point3d(
							point.getCoordinates().get(0).getValue(),
							point.getCoordinates().get(1).getValue(),
							point.getCoordinates().get(2).getValue()
					);
					
					if(!first) {
						distanceMeasured += previouse.distance(currentP);
					}else {
						first = false;
					}
					
					previouse = currentP;
				}
				
				resultValues.add(distanceMeasured);
				
				
			}
		}
		
		
		if(resultValues.size()==1) {
			setResult(0, resultValues.get(0));
		} else {
			setResult(0, resultValues);
		}

	}
	
}
