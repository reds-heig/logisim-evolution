package com.cburch.logisim.vhdl.base;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import com.cburch.logisim.data.AttributeSet;
import com.cburch.logisim.fpga.designrulecheck.Netlist;
import com.cburch.logisim.fpga.designrulecheck.NetlistComponent;
import com.cburch.logisim.fpga.fpgagui.FPGAReport;
import com.cburch.logisim.fpga.hdlgenerator.AbstractHDLGeneratorFactory;
import com.cburch.logisim.fpga.hdlgenerator.FileWriter;
import com.cburch.logisim.fpga.hdlgenerator.HDLGeneratorFactory;
import com.cburch.logisim.instance.Port;

public class VhdlHDLGeneratorFactory extends AbstractHDLGeneratorFactory  {


	@Override
	public ArrayList<String> GetArchitecture(Netlist TheNetlist,
			AttributeSet attrs, String ComponentName, FPGAReport Reporter,
			String HDLType) {
		ArrayList<String> contents = new ArrayList<String>();
		contents.addAll(FileWriter.getGenerateRemark(ComponentName, HDLType,
				TheNetlist.projName()));

		VhdlContent content = (VhdlContent) attrs
				.getValue(VhdlEntity.CONTENT_ATTR);
		contents.add(content.getLibraries());
		contents.add(content.getArchitecture());

		return contents;
	}

	@Override
	public String getComponentStringIdentifier() {
		return "VHDL";
	}

	@Override
	public SortedMap<String, Integer> GetInputList(Netlist TheNetlist,
			AttributeSet attrs) {
		SortedMap<String, Integer> inputs = new TreeMap<String, Integer>();

		Port[] rawInputs = attrs.getValue(VhdlEntity.CONTENT_ATTR).getInputs();
		for (int i = 0; i < rawInputs.length; i++)
			inputs.put(rawInputs[i].getToolTip(), rawInputs[i]
					.getFixedBitWidth().getWidth());

		return inputs;
	}

	@Override
	public SortedMap<String, Integer> GetOutputList(Netlist TheNetlist,
			AttributeSet attrs) {
		SortedMap<String, Integer> outputs = new TreeMap<String, Integer>();

		Port[] rawOutputs = attrs.getValue(VhdlEntity.CONTENT_ATTR)
				.getOutputs();
		for (int i = 0; i < rawOutputs.length; i++)
			outputs.put(rawOutputs[i].getToolTip(), rawOutputs[i]
					.getFixedBitWidth().getWidth());

		return outputs;
	}

	@Override
	public SortedMap<String, String> GetPortMap(Netlist Nets,
			NetlistComponent ComponentInfo, FPGAReport Reporter, String HDLType) {
		SortedMap<String, String> PortMap = new TreeMap<String, String>();

		AttributeSet attrs = ComponentInfo.GetComponent().getAttributeSet();
		VhdlContent content = attrs.getValue(VhdlEntity.CONTENT_ATTR);

		Port[] inputs = content.getInputs();
		Port[] outputs = content.getOutputs();

		for (int i = 0; i < inputs.length; i++)
			PortMap.putAll(GetNetMap(inputs[i].getToolTip(), true,
					ComponentInfo, i, Reporter, HDLType, Nets));
		for (int i = 0; i < outputs.length; i++)
			PortMap.putAll(GetNetMap(outputs[i].getToolTip(), true,
					ComponentInfo, i + inputs.length, Reporter, HDLType, Nets));

		return PortMap;
	}

	@Override
	public String GetSubDir() {
		return "circuit";
	}

	@Override
	public boolean HDLTargetSupported(String HDLType, AttributeSet attrs) {
		return HDLType.equals(HDLGeneratorFactory.VHDL);
	}

}