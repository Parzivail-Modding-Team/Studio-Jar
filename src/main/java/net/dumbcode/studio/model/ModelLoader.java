package net.dumbcode.studio.model;

import net.dumbcode.studio.util.ByteBuffer;
import net.dumbcode.studio.util.RotationReorder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class ModelLoader {
    public static ModelInfo loadModel(InputStream stream) throws IOException {
        return loadModel(stream, RotationOrder.ZYX);
    }
    public static ModelInfo loadModel(InputStream stream, RotationOrder order) throws IOException {
        Objects.requireNonNull(order, "Rotation Order is null");

        ByteBuffer buffer = new ByteBuffer(stream);
        RotationOrder current = RotationOrder.ZYX; //TODO: part of the format.

        ModelInfo info = new ModelInfo(buffer.readInt(), buffer.readString(), buffer.readInt(), buffer.readInt(), order);
        readCubeArray(info, buffer, current, order, info.getRoots());


        return info;
    }

    private static void readCubeArray(ModelInfo parent, ByteBuffer buffer, RotationOrder current, RotationOrder target, List<CubeInfo> list) throws IOException {
        int size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            CubeInfo info = new CubeInfo(
                parent,
                buffer.readString(),
                new int[] { buffer.readInt(), buffer.readInt(), buffer.readInt() },
                new float[] { buffer.readFloat(), buffer.readFloat(), buffer.readFloat() },
                new float[] { buffer.readFloat(), buffer.readFloat(), buffer.readFloat() },
                new float[] { buffer.readFloat(), buffer.readFloat(), buffer.readFloat() },
                new int[] { buffer.readInt(), buffer.readInt() },
                buffer.readBoolean(),
                new float[] { buffer.readFloat(), buffer.readFloat(), buffer.readFloat() }
            );
            RotationReorder.reorder(info.getRotation(), current, target);
            readCubeArray(parent, buffer, current, target, info.getChildren());
            list.add(info);
        }
    }

}
