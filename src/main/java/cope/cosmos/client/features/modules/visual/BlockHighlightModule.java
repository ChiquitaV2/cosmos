package cope.cosmos.client.features.modules.visual;

import cope.cosmos.client.events.render.player.RenderSelectionBoxEvent;
import cope.cosmos.client.features.modules.Category;
import cope.cosmos.client.features.modules.Module;
import cope.cosmos.client.features.setting.Setting;
import cope.cosmos.util.render.RenderBuilder;
import cope.cosmos.util.render.RenderBuilder.Box;
import cope.cosmos.util.render.RenderUtil;
import cope.cosmos.util.string.ColorUtil;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author linustouchtips
 * @since 03/08/2022
 */
public class BlockHighlightModule extends Module {
    public static BlockHighlightModule INSTANCE;

    public BlockHighlightModule() {
        super("BlockHighlight", Category.VISUAL, "Highlights the block the player is focused on");
        INSTANCE = this;
    }

    // **************************** render ****************************

    public static Setting<Box> renderMode = new Setting<>("RenderMode", Box.OUTLINE)
            .setDescription("Style of the visual").setExclusion(Box.GLOW, Box.REVERSE);

    public static Setting<Float> lineWidth = new Setting<>("LineWidth", 0.1F, 1.5F, 5F, 1)
            .setDescription("Width of the visual");

    @Override
    public void onRender3D() {

        // raytrace to what we are facing
        RayTraceResult mouseOver = mc.objectMouseOver;

        // check if we are focused on a block
        if (mouseOver != null && mouseOver.typeOfHit.equals(Type.BLOCK)) {

            // box of the mouse over object
            AxisAlignedBB mouseOverBox = mc.world.getBlockState(mouseOver.getBlockPos()).getSelectedBoundingBox(mc.world, mouseOver.getBlockPos());

            // draw box highlight
            RenderUtil.drawBox(new RenderBuilder()
                    .position(mouseOverBox)
                    .color(ColorUtil.getPrimaryAlphaColor(60))
                    .box(renderMode.getValue())
                    .setup()
                    .line(lineWidth.getValue())
                    .depth(true)
                    .blend()
                    .texture()
            );
        }
    }

    @SubscribeEvent
    public void onRenderSelectionBox(RenderSelectionBoxEvent event) {

        // prevent vanilla selection box from rendering
        event.setCanceled(true);
    }
}
