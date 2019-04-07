package jadx.core.dex.visitors;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jadx.core.dex.info.MethodInfo;
import jadx.core.dex.instructions.ConstStringNode;
import jadx.core.dex.instructions.InsnType;
import jadx.core.dex.instructions.InvokeNode;
import jadx.core.dex.instructions.args.InsnArg;
import jadx.core.dex.instructions.args.InsnWrapArg;
import jadx.core.dex.nodes.BlockNode;
import jadx.core.dex.nodes.InsnNode;
import jadx.core.dex.nodes.MethodNode;
import java.util.Base64;

public class DecodeStrings extends AbstractVisitor {

  private static final Logger LOG = LoggerFactory.getLogger(DecodeStrings.class);

  public static String decode(String input) {
    String KEY_STR = "iytDlJSoOg";
    byte[] KEY = KEY_STR.getBytes();
    byte[] b64decoded = Base64.getDecoder().decode(input.getBytes());

    byte[] out = new byte[b64decoded.length];

    for (int i = 0; i < out.length; i++) {
      out[i] = (byte) (b64decoded[i] ^ KEY[i % KEY.length]);
    }
    return new String(out);
  }

  @Override
  public void visit(MethodNode mth) {
    if (mth.isNoCode()) {
      return;
    }
    for (BlockNode block : mth.getBasicBlocks()) {
      simplify(mth, block);
    }
  }


  private static InsnNode simplify(MethodNode mth, InsnNode insn) {
    for (InsnArg arg : insn.getArguments()) {
      if (arg.isInsnWrap()) {
        InsnNode ni = simplify(mth, ((InsnWrapArg) arg).getWrapInsn());
        if (ni != null) {
          arg.wrapInstruction(ni);
        }
      }
    }

    if (insn.getType() != InsnType.INVOKE) {
      return null;
    }

    MethodInfo callMth = ((InvokeNode) insn).getCallMth();

    if (callMth.getDeclClass().getFullName().equals("pykqdxlnyt.iytDlJSoOg") && callMth.getName().equals("GaoAoxCoJpRm")) {
      // GaoAoxCoJpRm is a "virtual" method:
      // arg0: this
      // arg1: String
      InsnArg arg0 = insn.getArg(1);
      ConstStringNode string_inst = (ConstStringNode)(((InsnWrapArg)arg0).getWrapInsn());
      String encoded = string_inst.getString();
      String decoded = decode(encoded);

      LOG.info("{} --> {}", encoded, decoded);

      // New string
      ConstStringNode constStrInsn = new ConstStringNode(decode(encoded));
      return constStrInsn;
    }

    return null;

  }

  private static void simplify(MethodNode mth, BlockNode block) {
    List<InsnNode> insns = block.getInstructions();
    int size = insns.size();
    for (int i = 0; i < size; i++) {
      InsnNode insn = insns.get(i);
      simplify(mth, insn);
    }
  }
}
