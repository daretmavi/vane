package org.oddlama.vane.portals.portal

import org.bukkit.block.Block
import org.json.JSONObject
import org.oddlama.vane.core.persistent.PersistentSerializer
import org.oddlama.vane.util.LazyBlock
import java.io.IOException
import java.util.*

/**
 * Represents a typed block belonging to a portal structure.
 *
 * @property block lazy block reference.
 * @property type semantic role of the block in the portal.
 */
class PortalBlock(private val block: LazyBlock, private val type: Type?) {
    /** Creates a portal block from a Bukkit block instance. */
    constructor(block: Block?, type: Type?) : this(LazyBlock(block), type)
    
    /** Resolves and returns the referenced Bukkit block. */
    fun block() = block.block()
    
    /** Returns the semantic portal block type. */
    fun type() = type
    
    /** Creates a lightweight lookup payload for this block and [portalId]. */
    fun lookup(portalId: UUID?) = PortalBlockLookup(portalId, type)
    
    /** Returns hash code based on block identity. */
    override fun hashCode() = block().hashCode()
    
    /** Returns true when [other] refers to the same block position. */
    override fun equals(other: Any?): Boolean {
        return other is PortalBlock && block() == other.block()
        
        // Only block is compared, as the same block can only have one functions.
    }
    
    /** Semantic role of a block inside portal structures. */
    enum class Type {
        ORIGIN,
        CONSOLE,
        BOUNDARY1,
        BOUNDARY2,
        BOUNDARY3,
        BOUNDARY4,
        BOUNDARY5,
        PORTAL,
    }
    
    /** JSON serializer helpers for [PortalBlock]. */
    companion object {
        /** Serializes a portal block into a JSON object. */
        @JvmStatic
        @Throws(IOException::class)
        fun serialize(o: Any): Any {
            val portalBlock = o as PortalBlock
            val json = JSONObject()
            
            json.put(
                "block",
                PersistentSerializer.toJson(
                    LazyBlock::class.java,
                    portalBlock.block
                )
            )
            
            json.put(
                "type",
                PersistentSerializer.toJson(
                    Type::class.java,
                    portalBlock.type
                )
            )
            
            return json
        }
        
        /**
         * Deserializes a portal block from a JSON object.
         *
         * Supports both the current enum names:
         *   BOUNDARY1, BOUNDARY2, ..., BOUNDARY5
         *
         * and legacy enum names:
         *   BOUNDARY_1, BOUNDARY_2, ..., BOUNDARY_5
         */
        @JvmStatic
        @Throws(IOException::class)
        fun deserialize(o: Any): PortalBlock {
            val json = o as JSONObject
            
            val block: LazyBlock? =
            PersistentSerializer.fromJson(
                LazyBlock::class.java,
                json.get("block")
            )
            
            val type: Type? =
            if (json.isNull("type")) {
                null
            } else {
                deserializeType(json.getString("type"))
            }
            
            return PortalBlock(block!!, type)
        }
        
        /**
         * Deserializes a portal block type.
         *
         * Accepts both the current format (BOUNDARY1) and
         * the legacy format (BOUNDARY_1).
         */
        private fun deserializeType(value: String): Type {
            return when (value) {
                "BOUNDARY_1" -> Type.BOUNDARY1
                "BOUNDARY_2" -> Type.BOUNDARY2
                "BOUNDARY_3" -> Type.BOUNDARY3
                "BOUNDARY_4" -> Type.BOUNDARY4
                "BOUNDARY_5" -> Type.BOUNDARY5
                else -> Type.valueOf(value)
            }
        }
    }
}